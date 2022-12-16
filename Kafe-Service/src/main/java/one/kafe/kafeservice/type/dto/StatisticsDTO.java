package one.kafe.kafeservice.type.dto;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StatisticsDTO {
	private Map<String, Long> countryName; // 나라 이름
	private Map<String, Long> device; // 사용자 기기
	private Map<String, Long> useragent; // 접속 브라우저
	private Map<String, Long> httpReferer; // HTTP Referer 정보
	private Map<String, Coordinates> coordinates; // 좌표
	private List<String> timestamp; // timestamp
}
