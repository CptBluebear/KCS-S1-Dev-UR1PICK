package one.kafe.kafeservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.type.entity.EsIndex;
import one.kafe.kafeservice.type.vo.StatisticsVo;

public interface DocumentService {
	List<EsIndex> getSearchAll() throws IOException; // 전체에 대한 조회

	List<EsIndex> findByUrl(String url);

	Long getUrlCnt(String url);

	StatisticsVo statistic(Long seq, Long userSeq) throws IOException, SearchResultNotExistException,
		PermissionDeniedException;

	Map<String, Map<String, Long>> weeklyReport(String url, LocalDateTime startDate, LocalDateTime endDate) throws IOException;
}