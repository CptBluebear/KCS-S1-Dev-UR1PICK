package org.corodiak.kcss1devt1auth.handler;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.corodiak.kcss1devt1auth.auth.exception.UnAuthorizeException;
import org.corodiak.kcss1devt1auth.exception.EmailDuplicateException;
import org.corodiak.kcss1devt1auth.type.dto.ResponseModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.gson.stream.MalformedJsonException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice(basePackages = "org.corodiak.kcss1devt1auth.controller")
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler({
		EmailDuplicateException.class
	})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseModel emailDuplicateError(HttpServletRequest request, HttpServletResponse response, Exception e) {
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.BAD_REQUEST)
			.message("Already Exist Email Account.")
			.build();
		return responseModel;
	}

	@ExceptionHandler({
		NoSuchElementException.class,
		MissingServletRequestParameterException.class,
		MalformedJsonException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		NullPointerException.class,
		DataIntegrityViolationException.class,
		MethodArgumentNotValidException.class
	})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseModel parameterError(HttpServletRequest request, HttpServletResponse response, Exception e) {
		e.printStackTrace();
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.BAD_REQUEST)
			.message("요청에 필요한 파라미터가 비어있거나 적절하지 않습니다.")
			.build();
		response.setStatus(400);
		return responseModel;
	}

	@ExceptionHandler({
		UnAuthorizeException.class,
		AccessDeniedException.class,
		UsernameNotFoundException.class
	})
	public ResponseModel unAuthorizeRequestError(HttpServletRequest request, HttpServletResponse response,
		Exception e) {
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.UNAUTHORIZED)
			.message("인증되지 않은 사용자이거나 권한이 부족합니다.")
			.build();
		response.setStatus(401);
		return responseModel;
	}

	@ExceptionHandler(Exception.class)
	public ResponseModel unExceptedError(HttpServletRequest request, HttpServletResponse response, Exception e) {
		e.printStackTrace();
		response.setStatus(500);
		return ResponseModel.builder()
			.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
			.message("예기치 못한 오류 발생")
			.build();
	}
}
