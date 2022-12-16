package one.kafe.kafeservice.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

import one.kafe.kafeservice.exception.NotAllowUrlException;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.type.dto.ModifiedSUrlDto;
import one.kafe.kafeservice.type.dto.SUrlDto;
import one.kafe.kafeservice.type.vo.SUrlVo;

public interface URLService {
	String createURL(SUrlDto shortURLDto, String ip, Long userSeq) throws
		PermissionDeniedException,
		NotAllowUrlException,
		DataIntegrityViolationException;

	boolean checkDuplicateShortUrl(String shortUrl);

	void removeShortUrl(Long seq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException;

	List<SUrlVo.SimpleInfo> getSUrlListByUserSeq(Long userSeq);

	boolean modifyShortUrl(ModifiedSUrlDto sUrlDto, Long shortUrlSeq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException, NotAllowUrlException;

	SUrlVo.DetailInfo getShortUrlBySeq(Long seq, Long userSeq) throws
		SearchResultNotExistException,
		PermissionDeniedException;

	boolean switchShortUrlIsActive(Long shortUrlSeq, boolean isActive, Long userSeq) throws
		SearchResultNotExistException, PermissionDeniedException;
}
