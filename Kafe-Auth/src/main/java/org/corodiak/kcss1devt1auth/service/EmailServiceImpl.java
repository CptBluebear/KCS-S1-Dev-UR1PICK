package org.corodiak.kcss1devt1auth.service;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.corodiak.kcss1devt1auth.repository.EmailVerificationRepository;
import org.corodiak.kcss1devt1auth.repository.UserRepository;
import org.corodiak.kcss1devt1auth.type.entity.EmailVerification;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.corodiak.kcss1devt1auth.type.etc.UserStatus;
import org.corodiak.kcss1devt1auth.type.vo.EmailTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final RabbitTemplate rabbitTemplate;

	private final EmailVerificationRepository emailVerificationRepository;
	private final UserRepository userRepository;

	public void sendEmailValidation(String name, String receiver, String code) {
		Map<String, String> properties = new HashMap<>();
		properties.put("name", name);
		properties.put("receiver", receiver);
		properties.put("code", code);
		EmailTemplate emailTemplate = new EmailTemplate("validation", properties);
		rabbitTemplate.convertAndSend("email.exchange", "email.key", emailTemplate);
	}

	@Transactional
	public boolean verifyEmail(String code) {
		try {
			EmailVerification emailVerification = emailVerificationRepository.findByCode(code);
			if (emailVerification != null) {
				User user = emailVerification.getUser();
				user.setUserStatus(UserStatus.VERIFIED);
				userRepository.save(user);
				emailVerificationRepository.delete(emailVerification);
				return true;
			}
		} catch (IllegalArgumentException ignored) {
			return false;
		}
		return false;
	}

}
