package one.kafe.kafeservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.repository.SUrlRepository;
import one.kafe.kafeservice.repository.UserRepository;
import one.kafe.kafeservice.type.entity.User;
import one.kafe.kafeservice.type.vo.ReportTemplate;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final DocumentService documentService;
	private final URLService urlService;
	private final UserRepository userRepository;

	private final SUrlRepository sUrlRepository;
	private final RabbitTemplate rabbitTemplate;

	public void generateReportBase(Long userSeq, LocalDateTime base) throws IOException {
		User user = userRepository.findById(userSeq).get();
		List<Tuple> urlData = sUrlRepository.findShortUrlByUserSeq(userSeq);
		long totalAccessCount = 0L;
		Map<String, Long> browser = new HashMap<>();
		Map<String, Long> os = new HashMap<>();
		Map<String, Long> sns = new HashMap<>();
		Map<String, Long> dayOfWeek = new HashMap<>();
		for(Tuple tuple:urlData) {
			String shortUrl = tuple.get(0, String.class);
			totalAccessCount += tuple.get(1, Long.class);
			LocalDateTime registerDate = tuple.get(2, LocalDateTime.class);
			LocalDateTime startDate = base.minusMonths(1);
			if(startDate.isBefore(registerDate)) {
				startDate = registerDate;
			}
			//나중에 삭제할 디버그 코드
			startDate = base.minusMonths(1);

			Map<String, Map<String, Long>> result = documentService.weeklyReport(shortUrl, startDate, base);
			Map<String, Long> browserResult = result.get("Browser");
			for(String key:browserResult.keySet()) {
				browser.put(key, browser.getOrDefault(key, 0L) + browserResult.get(key));
			}
			Map<String, Long> osResult = result.get("OS");
			for(String key:osResult.keySet()) {
				os.put(key, os.getOrDefault(key, 0L) + osResult.get(key));
			}
			Map<String, Long> snsResult = result.get("Sns");
			for(String key:snsResult.keySet()) {
				sns.put(key, sns.getOrDefault(key, 0L) + snsResult.get(key));
			}
			Map<String, Long> dayOfWeekResult = result.get("DayOfWeek");
			for(String key:dayOfWeekResult.keySet()) {
				dayOfWeek.put(key, dayOfWeek.getOrDefault(key, 0L) + dayOfWeekResult.get(key));
			}
		}
		ReportTemplate reportTemplate = new ReportTemplate(user.getName(), user.getEmail(), totalAccessCount, browser, os, sns, dayOfWeek);
		rabbitTemplate.convertAndSend("report.exchange", "report.key", reportTemplate);
	}

}
