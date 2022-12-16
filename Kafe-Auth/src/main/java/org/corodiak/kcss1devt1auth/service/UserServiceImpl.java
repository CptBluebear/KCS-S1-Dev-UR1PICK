package org.corodiak.kcss1devt1auth.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.corodiak.kcss1devt1auth.auth.jwt.AuthToken;
import org.corodiak.kcss1devt1auth.auth.jwt.AuthTokenProvider;
import org.corodiak.kcss1devt1auth.exception.EmailDuplicateException;
import org.corodiak.kcss1devt1auth.repository.AccountRepository;
import org.corodiak.kcss1devt1auth.repository.EmailVerificationRepository;
import org.corodiak.kcss1devt1auth.repository.UserRepository;
import org.corodiak.kcss1devt1auth.type.dto.SignInDto;
import org.corodiak.kcss1devt1auth.type.dto.SignUpDto;
import org.corodiak.kcss1devt1auth.type.dto.UserInfoUpdateDto;
import org.corodiak.kcss1devt1auth.type.dto.UserPasswordUpdateDto;
import org.corodiak.kcss1devt1auth.type.entity.Account;
import org.corodiak.kcss1devt1auth.type.entity.EmailVerification;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.corodiak.kcss1devt1auth.type.etc.Role;
import org.corodiak.kcss1devt1auth.type.etc.UserStatus;
import org.corodiak.kcss1devt1auth.type.vo.UserVo;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final long TOKEN_DURATION = 1000L * 60L * 60L * 24L;

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthTokenProvider authTokenProvider;
	private final EmailVerificationRepository emailVerificationRepository;

	private final EmailService emailService;

	@Override
	@Transactional
	public AuthToken signup(SignUpDto signUpDto) throws EmailDuplicateException {
		User user = User.builder()
			.name(signUpDto.getName())
			.email(signUpDto.getEmail())
			.role(Role.USER)
			.userStatus(UserStatus.NOT_VERIFIED)
			.lastLogin(LocalDateTime.now())
			.plan("1")
			.build();
		user = userRepository.save(user);

		Account account = Account.builder()
			.email(signUpDto.getEmail())
			.pw(passwordEncoder.encode(signUpDto.getPassword()))
			.user(user)
			.build();

		try {
			accountRepository.save(account);
		} catch (DataIntegrityViolationException e) {
			throw new EmailDuplicateException(user.getEmail());
		}

		EmailVerification emailVerification = EmailVerification.builder()
			.user(user)
			.code(EmailVerification.generate())
			.build();
		emailVerificationRepository.save(emailVerification);
		emailService.sendEmailValidation(user.getName(), user.getEmail(), emailVerification.getCode());

		Date expiry = new Date();
		expiry.setTime(expiry.getTime() + (TOKEN_DURATION));
		return authTokenProvider.createToken(Long.toString(user.getSeq()), Role.USER.toString(), expiry);
	}

	@Override
	@Transactional
	public void resign(Long userSeq) {
		Optional<User> user = userRepository.findById(userSeq);
		if (user.isPresent()) {
			User result = user.get();
			result.setUserStatus(UserStatus.RESIGNED);
			userRepository.save(result);
		}
	}

	@Override
	@Transactional
	public void userInfoUpdate(Long userSeq, UserInfoUpdateDto userInfoUpdateDto) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findById(userSeq);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(userSeq.toString());
		}
		User result = user.get();
		result.setEmail(userInfoUpdateDto.getEmail());
		result.setName(userInfoUpdateDto.getName());
		try {
			userRepository.save(result);
		} catch (ConstraintViolationException e) {
			throw new EmailDuplicateException(result.getEmail());
		}
	}

	@Override
	@Transactional
	public void userPasswordUpdate(Long userSeq, UserPasswordUpdateDto userPasswordUpdateDto) throws
		UsernameNotFoundException {
		User user = User.builder().seq(userSeq).build();
		Optional<Account> account = accountRepository.findByUser(user);
		if (account.isEmpty()) {
			throw new UsernameNotFoundException(userSeq.toString());
		}
		Account result = account.get();
		result.setPw(passwordEncoder.encode(userPasswordUpdateDto.getPassword()));
		accountRepository.save(result);
	}

	@Override
	@Transactional
	public AuthToken signIn(SignInDto signInDto) throws UsernameNotFoundException {
		Optional<Account> account = accountRepository.findByEmail(signInDto.getEmail());
		if (account.isEmpty()) {
			throw new UsernameNotFoundException(signInDto.getEmail());
		}
		if (passwordEncoder.matches(signInDto.getPassword(), account.get().getPw())) {
			Date expiry = new Date();
			expiry.setTime(expiry.getTime() + (TOKEN_DURATION));
			User user = account.get().getUser();
			return authTokenProvider.createToken(Long.toString(account.get().getUser().getSeq()),
				account.get().getUser().getRole().toString(), expiry);
		} else {
			throw new UsernameNotFoundException(signInDto.getEmail());
		}
	}

	@Override
	public UserVo getUserInfo(Long userSeq) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findById(userSeq);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(userSeq.toString());
		}
		return new UserVo(user.get());
	}
}
