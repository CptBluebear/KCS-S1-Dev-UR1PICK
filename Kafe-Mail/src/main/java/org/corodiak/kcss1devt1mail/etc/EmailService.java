package org.corodiak.kcss1devt1mail.etc;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.corodiak.kcss1devt1mail.type.dto.ChartTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;

	public void sendValidation(String title, Map<String, String> values) throws
		MessagingException, IOException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("Kafe");

		//메일 제목 설정
		helper.setSubject(title);

		//수신자 설정
		helper.setTo(values.get("receiver"));

		//템플릿에 전달할 데이터 설정
		Context context = new Context();
		values.forEach(context::setVariable);

		//메일 내용 설정 : 템플릿 프로세스
		String html = templateEngine.process("validation", context);
		helper.setText(html, true);

		//메일 보내기
		javaMailSender.send(message);
	}

	public void sendChart(String title, ChartTemplate chartTemplate) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("Kafe");

		//메일 제목 설정
		helper.setSubject(title);

		//수신자 설정
		helper.setTo(chartTemplate.getEmail());

		//템플릿에 전달할 데이터 설정
		Context context = new Context();
		context.setVariable("name", chartTemplate.getName());
		context.setVariable("browserData", chartTemplate.getBrowserData());
		context.setVariable("osData", chartTemplate.getOsData());
		context.setVariable("snsData", chartTemplate.getSnsData());
		context.setVariable("dayOfWeekData", chartTemplate.getDayOfWeekData());

		//메일 내용 설정 : 템플릿 프로세스
		String html = templateEngine.process("chart", context);
		helper.setText(html, true);

		//메일 보내기
		javaMailSender.send(message);
	}
}