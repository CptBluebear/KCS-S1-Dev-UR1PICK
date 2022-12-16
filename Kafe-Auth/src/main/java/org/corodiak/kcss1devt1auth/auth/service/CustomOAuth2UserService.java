package org.corodiak.kcss1devt1auth.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.corodiak.kcss1devt1auth.auth.factory.OAuth2UserInfoFactory;
import org.corodiak.kcss1devt1auth.exception.EmailDuplicateException;
import org.corodiak.kcss1devt1auth.repository.EmailVerificationRepository;
import org.corodiak.kcss1devt1auth.repository.OAuthUserRepository;
import org.corodiak.kcss1devt1auth.repository.UserRepository;
import org.corodiak.kcss1devt1auth.service.EmailService;
import org.corodiak.kcss1devt1auth.type.dto.OAuthUserInfo;
import org.corodiak.kcss1devt1auth.type.dto.UserPrincipal;
import org.corodiak.kcss1devt1auth.type.entity.EmailVerification;
import org.corodiak.kcss1devt1auth.type.entity.OAuthUser;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.corodiak.kcss1devt1auth.type.etc.OAuthProvider;
import org.corodiak.kcss1devt1auth.type.etc.Role;
import org.corodiak.kcss1devt1auth.type.etc.UserStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final OAuthUserRepository oAuthUserRepository;
	private final UserRepository userRepository;
	private final EmailVerificationRepository emailVerificationRepository;
	private final EmailService emailService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		return process(userRequest, oAuth2User);
	}

	OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		OAuthProvider oAuthProvider = OAuthProvider.valueOf(
			userRequest.getClientRegistration().getClientName().toUpperCase());
		OAuthUserInfo oAuth2UserInfo = OAuth2UserInfoFactory.of(oAuthProvider, oAuth2User.getAttributes());
		OAuth2User user = saveOrUpDate(oAuth2UserInfo);
		return user;
	}

	@Transactional
	OAuth2User saveOrUpDate(OAuthUserInfo oAuth2UserInfo) {
		Optional<OAuthUser> savedUser;
		savedUser = oAuthUserRepository.findByProviderUserIdAndOap(
			oAuth2UserInfo.getId(),
			OAuthProvider.valueOf(oAuth2UserInfo.getOAuthProviderName().toUpperCase())
		);
		User user;
		OAuthUser oAuthUser;
		if (savedUser.isPresent()) {
			oAuthUser = savedUser.get();
			user = oAuthUser.getUser();
			if (user.getUserStatus().equals(UserStatus.RESIGNED) || user.getUserStatus().equals(UserStatus.VANNED)) {
				return UserPrincipal.create(User.builder()
					.name("")
					.email("")
					.seq(-1L)
					.build());
			}
		} else {
			user = User.builder()
				.name(oAuth2UserInfo.getName())
				.email(oAuth2UserInfo.getEmail())
				.plan("1")
				.lastLogin(LocalDateTime.now())
				.userStatus(UserStatus.NOT_VERIFIED)
				.role(Role.USER)
				.build();
			try {
				user = userRepository.save(user);
			} catch (DataIntegrityViolationException e) {
				throw new EmailDuplicateException(user.getEmail());
			}

			EmailVerification emailVerification = EmailVerification.builder()
				.user(user)
				.code(EmailVerification.generate())
				.build();
			emailVerificationRepository.save(emailVerification);

			emailService.sendEmailValidation(user.getName(), user.getEmail(), emailVerification.getCode());

			oAuthUser = OAuthUser.builder()
				.providerUserId(oAuth2UserInfo.getId())
				.email(oAuth2UserInfo.getEmail())
				.name(oAuth2UserInfo.getName())
				.oap(OAuthProvider.valueOf(oAuth2UserInfo.getOAuthProviderName().toUpperCase()))
				.user(user)
				.build();
		}
		oAuthUserRepository.save(oAuthUser);
		return UserPrincipal.create(user);
	}
}
