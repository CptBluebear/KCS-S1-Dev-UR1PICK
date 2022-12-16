package one.kafe.kafeurlredirect.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeurlredirect.repository.SUrlRepository;
import one.kafe.kafeurlredirect.type.entity.SUrl;
import one.kafe.kafeurlredirect.type.vo.SUrlVo;

@Service
@RequiredArgsConstructor
public class UrlService {

	private final SUrlRepository sUrlRepository;

	public Optional<SUrlVo> urlRedirectProcess(String shortUrl) {
		Optional<SUrl> sUrl = sUrlRepository.findByShortUrl(shortUrl);
		return sUrl.map(SUrlVo::new);
	}

	@Transactional
	public void increaseAccessCount(Long seq) {
		sUrlRepository.updateAccessCountAndLastAccess(seq);
	}

}
