package one.kafe.kafeservice.job;

import java.io.IOException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kafe.kafeservice.util.UrlList;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlackListUpdateJob extends QuartzJobBean {

	private final UrlList urlList;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			urlList.updateBlackList();
			log.info("BlackList 갱신 성공");
		} catch (IOException ignored) {
			log.error("BlackList 갱신 실패");
		}
	}
}
