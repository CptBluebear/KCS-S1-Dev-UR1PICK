package one.kafe.kafeservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.auth.util.AuthUtil;
import one.kafe.kafeservice.exception.NotAllowUrlException;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.service.URLService;
import one.kafe.kafeservice.type.dto.ModifiedSUrlDto;
import one.kafe.kafeservice.type.dto.ResponseModel;
import one.kafe.kafeservice.type.dto.SUrlActiveDto;
import one.kafe.kafeservice.type.dto.SUrlDto;
import one.kafe.kafeservice.util.ExtractIPUtils;

@RestController
@RequiredArgsConstructor
public class UrlController {

	private final URLService urlService;

	@RequestMapping(value = "/api/shorturl", method = RequestMethod.POST)
	public ResponseModel generateShortUrl(@RequestBody @Valid SUrlDto sUrlDto, HttpServletRequest request) throws
		PermissionDeniedException, NotAllowUrlException {
		String ip = ExtractIPUtils.getIP(request);
		Long userSeq = AuthUtil.getAuthenticationInfoSeqOrAnonymousSeq();
		String result = urlService.createURL(sUrlDto, ip, userSeq);
		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("shortUrl", result);
		return responseModel;
	}

	@RequestMapping(value = "/api/shorturl/check/{shortUrl}", method = RequestMethod.GET)
	public ResponseModel checkUrlDuplicate(@PathVariable("shortUrl") String shortUrl) {
		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("check", urlService.checkDuplicateShortUrl(shortUrl));
		return responseModel;
	}

	@RequestMapping(value = "/api/shorturl/{shortUrlSeq}", method = RequestMethod.DELETE)
	public ResponseModel deleteUrl(@PathVariable("shortUrlSeq") Long shortUrlSeq) throws
		PermissionDeniedException,
		SearchResultNotExistException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		if (userSeq == -1L) {
			throw new PermissionDeniedException();
		}
		urlService.removeShortUrl(shortUrlSeq, userSeq);

		return ResponseModel.builder().build();
	}

	@RequestMapping(value = "/api/shorturl", method = RequestMethod.GET)
	public ResponseModel getShortUrlListByUser() throws PermissionDeniedException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeqOrAnonymousSeq();
		if (userSeq == -1L) {
			throw new PermissionDeniedException();
		}

		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("shortUrlList", urlService.getSUrlListByUserSeq(userSeq));
		return responseModel;
	}

	@RequestMapping(value = "/api/shorturl/{seq}", method = RequestMethod.PUT)
	public ResponseModel modifyShortUrl(@RequestBody ModifiedSUrlDto sUrlDto,
		@PathVariable("seq") Long shortUrlSeq) throws
		PermissionDeniedException,
		SearchResultNotExistException, NotAllowUrlException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		urlService.modifyShortUrl(sUrlDto, shortUrlSeq, userSeq);
		ResponseModel responseModel = ResponseModel.builder().build();
		return responseModel;
	}

	@RequestMapping(value = "/api/shorturl/{seq}", method = RequestMethod.GET)
	public ResponseModel getShortUrlBySeq(@PathVariable("seq") Long seq) throws
		PermissionDeniedException,
		SearchResultNotExistException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("shortUrl", urlService.getShortUrlBySeq(seq, userSeq));
		return responseModel;
	}

	@RequestMapping(value = "/api/shorturl/{seq}/active", method = RequestMethod.PUT)
	public ResponseModel switchShortUrlIsActive(@RequestBody SUrlActiveDto sUrlActiveDto,
		@PathVariable("seq") Long seq) throws
		PermissionDeniedException,
		SearchResultNotExistException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		urlService.switchShortUrlIsActive(seq, sUrlActiveDto.isActive(), userSeq);
		return ResponseModel.builder().build();
	}
}
