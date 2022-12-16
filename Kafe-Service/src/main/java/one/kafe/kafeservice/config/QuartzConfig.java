package one.kafe.kafeservice.config;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kafe.kafeservice.job.BlackListUpdateJob;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class QuartzConfig {

	private final Scheduler scheduler;

	@PostConstruct
	public void start() {
		JobDetail jobDetail = buildJobDetail(BlackListUpdateJob.class);
		try {
			scheduler.scheduleJob(jobDetail, buildJobTrigger("0 30 0/1 * * ?"));
			log.info("블랙리스트 업데이트 완료");
		} catch (SchedulerException e) {
			log.error("블랙리스트 배치 작업 생성 실패");
		}
	}

	public Trigger buildJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger()
			.withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
	}

	public JobDetail buildJobDetail(Class job) {
		return JobBuilder.newJob(job).build();
	}
}
