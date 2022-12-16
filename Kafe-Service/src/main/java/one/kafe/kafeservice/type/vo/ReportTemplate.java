package one.kafe.kafeservice.type.vo;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ReportTemplate {
	private String name;
	private String email;
	private Long accessCount;
	private Map<String, Long> browser;
	private Map<String, Long> os;
	private Map<String, Long> sns;
	private Map<String, Long> dayOfWeek;
}
