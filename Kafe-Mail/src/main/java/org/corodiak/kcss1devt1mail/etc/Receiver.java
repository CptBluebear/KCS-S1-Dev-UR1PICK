package org.corodiak.kcss1devt1mail.etc;

import java.io.IOException;

import javax.mail.MessagingException;

import org.corodiak.kcss1devt1mail.type.dto.ChartTemplate;
import org.corodiak.kcss1devt1mail.type.dto.EmailTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Receiver {

	private final EmailService emailService;

	@RabbitListener(queues = "email.queue")
	public void consumeValidation(EmailTemplate emailTemplate) throws MessagingException, IOException {
		if (emailTemplate.getType().equals("validation")) {
			emailService.sendValidation("Kafe.one 메일 인증", emailTemplate.getProperties());
		}
	}

	@RabbitListener(queues = "chart.queue")
	public void consumeChart(ChartTemplate chartTemplate) throws MessagingException {
		emailService.sendChart("Kafe.one 주간 리포트", chartTemplate);
	}
}
