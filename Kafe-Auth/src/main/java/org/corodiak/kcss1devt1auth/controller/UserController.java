package org.corodiak.kcss1devt1auth.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.corodiak.kcss1devt1auth.auth.jwt.AuthToken;
import org.corodiak.kcss1devt1auth.auth.util.AuthUtil;
import org.corodiak.kcss1devt1auth.service.EmailService;
import org.corodiak.kcss1devt1auth.service.UserService;
import org.corodiak.kcss1devt1auth.type.dto.ResponseModel;
import org.corodiak.kcss1devt1auth.type.dto.SignInDto;
import org.corodiak.kcss1devt1auth.type.dto.SignUpDto;
import org.corodiak.kcss1devt1auth.type.dto.UserInfoUpdateDto;
import org.corodiak.kcss1devt1auth.type.dto.UserPasswordUpdateDto;
import org.corodiak.kcss1devt1auth.type.vo.UserVo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final EmailService emailService;

	@RequestMapping(value = "/api/signup", method = RequestMethod.POST)
	public ResponseModel signup(@RequestBody @Valid SignUpDto signUpDto, HttpServletResponse response) {
		AuthToken authToken = userService.signup(signUpDto);
		ResponseModel responseModel = ResponseModel.builder()
			.message("Authorization Token Issued.")
			.build();
		response.setHeader("Authorization", authToken.getToken());
		return responseModel;
	}

	@RequestMapping(value = "/api/verify", method = RequestMethod.GET)
	public ResponseModel validUserEmail(@RequestParam("code") String code, HttpServletResponse response) throws
		IOException {
		ResponseModel responseModel;

		if (emailService.verifyEmail(code)) {
			responseModel = ResponseModel.builder().build();
			response.sendRedirect("https://kafe.one");
		} else {
			responseModel = ResponseModel.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message("Verification Code Invalid")
				.build();
			response.setStatus(404);
		}
		return responseModel;
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.DELETE)
	public ResponseModel resign() {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		userService.resign(userSeq);
		return ResponseModel.builder().build();
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.PUT)
	public ResponseModel userInfoUpdate(@RequestBody @Valid UserInfoUpdateDto userInfoUpdateDto) {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		userService.userInfoUpdate(userSeq, userInfoUpdateDto);
		return ResponseModel.builder().build();
	}

	@RequestMapping(value = "/api/user/password", method = RequestMethod.PUT)
	public ResponseModel userPasswordUpdate(@RequestBody @Valid UserPasswordUpdateDto userPasswordUpdateDto) {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		userService.userPasswordUpdate(userSeq, userPasswordUpdateDto);
		return ResponseModel.builder().build();
	}

	@RequestMapping(value = "/api/signin", method = RequestMethod.POST)
	public ResponseModel signin(@RequestBody SignInDto signInDto, HttpServletResponse response) {
		AuthToken authToken = userService.signIn(signInDto);
		ResponseModel responseModel = ResponseModel.builder()
			.message("Authorization Token Issued.")
			.build();
		response.setHeader("Authorization", authToken.getToken());
		return responseModel;
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.GET)
	public ResponseModel responseModel() {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		UserVo userVo = userService.getUserInfo(userSeq);
		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("user", userVo);
		return responseModel;
	}
}
