package one.kafe.kafeservice.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.exception.NotAllowUrlException;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.repository.DeletedSUrlRepository;
import one.kafe.kafeservice.repository.SUrlRepository;
import one.kafe.kafeservice.type.dto.ModifiedSUrlDto;
import one.kafe.kafeservice.type.dto.SUrlDto;
import one.kafe.kafeservice.type.entity.DeletedSurl;
import one.kafe.kafeservice.type.entity.SUrl;
import one.kafe.kafeservice.type.entity.User;
import one.kafe.kafeservice.type.etc.URLStatus;
import one.kafe.kafeservice.type.vo.PreviewTemplate;
import one.kafe.kafeservice.type.vo.SUrlVo;
import one.kafe.kafeservice.util.RandomStringGenerator;

@Service
@RequiredArgsConstructor
public class URLServiceImpl implements URLService {

	private final static Set<String> NOT_AVAILABLE_SHORT_URL = new HashSet<>(Arrays.asList("api", "signup", "static", "login", "list", "stat"));

	private final SUrlRepository sUrlRepository;
	private final DeletedSUrlRepository deletedSUrlRepository;

	private final URLValidationServiceImpl urlValidationService;

	private final PasswordEncoder passwordEncoder;

	private final RabbitTemplate rabbitTemplate;

	@Override
	@Transactional
	public String createURL(SUrlDto sUrlDto, String ip, Long userSeq) throws
		PermissionDeniedException,
		NotAllowUrlException,
		DataIntegrityViolationException {
		if ((sUrlDto.isCustomURL() || sUrlDto.hasPassword()) && userSeq == -1L) {
			throw new PermissionDeniedException();
		}
		if (!sUrlDto.isCustomURL()) {
			sUrlDto.setShortUrl(RandomStringGenerator.generateString(6));
		}
		if (sUrlDto.hasPassword()) {
			sUrlDto.setPassword(passwordEncoder.encode(sUrlDto.getPassword()));
		}

		URLStatus urlStatus = urlValidationService.validURL(sUrlDto.getOriginalUrl());
		if (urlStatus.equals(URLStatus.LOOP_REDIRECT)) {
			throw new NotAllowUrlException();
		}
		SUrl shortURL = SUrl.builder()
			.shortUrl(sUrlDto.getShortUrl())
			.originalUrl(sUrlDto.getOriginalUrl())
			.startDate(sUrlDto.getStartDate())
			.endDate(sUrlDto.getEndDate())
			.accessCount(0L)
			.password(sUrlDto.getPassword())
			.ownerIp(ip)
			.lastAccess(LocalDateTime.now())
			.isCustomUrl(sUrlDto.isCustomURL())
			.hasPassword(sUrlDto.hasPassword())
			.registerDate(LocalDateTime.now())
			.urlStatus(urlStatus)
			.preview("https://kafe-one-preview.s3.ap-northeast-2.amazonaws.com/preview-dummy.png")
			.user(User.builder().seq(userSeq).build())
			.isActive(true)
			.build();

		do {
			try {
				sUrlRepository.save(shortURL);
				break;
			} catch (DataIntegrityViolationException e) {
				if (shortURL.isCustomUrl()) {
					throw e;
				} else {
					shortURL.setShortUrl(RandomStringGenerator.generateString(6));
				}
			}
		} while (true);

		switch (urlStatus) {
			case NORMAL:
			case INSPECTING:
				break;
			case MALICIOUS_BLACKLIST:
			case MALICIOUS_CERT_NOT_VALID:
			case MALICIOUS_URL_SIMILAR:
				PreviewTemplate previewTemplate = new PreviewTemplate(shortURL.getSeq(), shortURL.getOriginalUrl());
				rabbitTemplate.convertAndSend("preview.exchange", "preview.key", previewTemplate);
				break;
		}

		return shortURL.getShortUrl();
	}

	@Override
	public boolean checkDuplicateShortUrl(String shortUrl) {
		return checkShortUrlAvailable(shortUrl) || sUrlRepository.existsByShortUrl(shortUrl);
	}

	@Override
	@Transactional
	public void removeShortUrl(Long seq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException {
		Optional<SUrl> sUrl = sUrlRepository.findByIdWithUser(seq);
		if (sUrl.isEmpty()) {
			throw new SearchResultNotExistException();
		}
		SUrl result = sUrl.get();
		if (result.getUser().getSeq() != userSeq) {
			throw new PermissionDeniedException();
		}

		DeletedSurl deletedSurl = DeletedSurl.builder()
			.originalSeq(result.getSeq())
			.shortUrl(result.getShortUrl())
			.originalUrl(result.getOriginalUrl())
			.password(result.getPassword())
			.isCustomURL(result.isCustomUrl())
			.hasPassword(result.hasPassword())
			.startDate(result.getStartDate())
			.endDate(result.getEndDate())
			.lastAccess(result.getLastAccess())
			.accessCount(result.getAccessCount())
			.ownerIp(result.getOwnerIp())
			.registerDate(result.getRegisterDate())
			.urlStatus(result.getUrlStatus())
			.user(result.getUser())
			.build();

		deletedSUrlRepository.save(deletedSurl);
		sUrlRepository.delete(result);
	}

	@Override
	public List<SUrlVo.SimpleInfo> getSUrlListByUserSeq(Long userSeq) {
		List<SUrl> sUrlList = sUrlRepository.findByUserSeq(userSeq);
		List<SUrlVo.SimpleInfo> results = sUrlList.stream()
			.map(SUrlVo.SimpleInfo::new)
			.collect(Collectors.toList());
		return results;
	}

	@Override
	@Transactional
	public boolean modifyShortUrl(ModifiedSUrlDto sUrlDto, Long shortUrlSeq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException, NotAllowUrlException {

		Optional<SUrl> sUrl = sUrlRepository.findByIdWithUser(shortUrlSeq);
		if (sUrl.isEmpty()) {
			throw new SearchResultNotExistException();
		}
		SUrl result = sUrl.get();
		if (result.getUser().getSeq() != userSeq) {
			throw new PermissionDeniedException();
		}
		if (sUrlDto.hasPassword()) {
			sUrlDto.setPassword(passwordEncoder.encode(sUrlDto.getPassword()));
		} else {
			sUrlDto.setPassword("");
		}

		URLStatus urlStatus = urlValidationService.validURL(sUrlDto.getOriginalUrl());
		if (urlStatus.equals(URLStatus.LOOP_REDIRECT)) {
			throw new NotAllowUrlException();
		}

		result.setOriginalUrl(sUrlDto.getOriginalUrl());
		result.setStartDate(sUrlDto.getStartDate());
		result.setEndDate(sUrlDto.getEndDate());
		result.hasPassword(sUrlDto.hasPassword());
		result.setPassword(sUrlDto.getPassword());
		result.setUrlStatus(urlStatus);

		switch (urlStatus) {
			case NORMAL:
			case INSPECTING:
				break;
			case MALICIOUS_BLACKLIST:
			case MALICIOUS_CERT_NOT_VALID:
			case MALICIOUS_URL_SIMILAR:
				PreviewTemplate previewTemplate = new PreviewTemplate(result.getSeq(), result.getOriginalUrl());
				rabbitTemplate.convertAndSend("preview.exchange", "preview.key", previewTemplate);
				break;
		}

		sUrlRepository.save(result);

		return true;
	}

	@Override
	public SUrlVo.DetailInfo getShortUrlBySeq(Long seq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException {
		Optional<SUrl> sUrl = sUrlRepository.findByIdWithUser(seq);
		if (sUrl.isEmpty()) {
			throw new SearchResultNotExistException();
		}
		SUrl result = sUrl.get();
		if (result.getUser().getSeq() != userSeq) {
			throw new PermissionDeniedException();
		}
		return new SUrlVo.DetailInfo(result);
	}

	@Override
	public boolean switchShortUrlIsActive(Long shortUrlSeq, boolean isActive, Long userSeq) throws
		SearchResultNotExistException, PermissionDeniedException {
		Optional<SUrl> sUrl = sUrlRepository.findByIdWithUser(shortUrlSeq);
		if (sUrl.isEmpty()) {
			throw new SearchResultNotExistException();
		}
		SUrl result = sUrl.get();
		if (result.getUser().getSeq() != userSeq) {
			throw new PermissionDeniedException();
		}
		result.isActive(isActive);
		sUrlRepository.save(result);
		return true;
	}

	private boolean checkShortUrlAvailable(String shortUrl) {
		return NOT_AVAILABLE_SHORT_URL.contains(shortUrl);
	}
}
