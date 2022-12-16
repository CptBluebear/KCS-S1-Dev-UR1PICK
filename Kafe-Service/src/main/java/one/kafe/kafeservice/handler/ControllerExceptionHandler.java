package one.kafe.kafeservice.handler;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.gson.stream.MalformedJsonException;

import lombok.extern.slf4j.Slf4j;
import one.kafe.kafeservice.auth.exception.UnAuthorizeException;
import one.kafe.kafeservice.exception.NotAllowUrlException;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.type.dto.ResponseModel;

@RestControllerAdvice(basePackages = "one.kafe.kafeservice.controller")
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler({
		NotAllowUrlException.class
	})
	public ResponseModel notAllowUrlError(HttpServletRequest request, HttpServletResponse response, Exception e) {
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.BAD_REQUEST)
			.message("허용되지 않은 URL 입니다.")
			.build();
		response.setStatus(400);
		return responseModel;
	}

	@ExceptionHandler({
		NoSuchElementException.class,
		MissingServletRequestParameterException.class,
		MalformedJsonException.class,
		HttpMessageNotReadableException.class,
		MethodArgumentTypeMismatchException.class,
		NullPointerException.class,
		DataIntegrityViolationException.class
	})
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
		PermissionDeniedException.class
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

	@ExceptionHandler({
		SearchResultNotExistException.class
	})
	public ResponseModel notFoundException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		ResponseModel responseModel = ResponseModel.builder()
			.httpStatus(HttpStatus.NOT_FOUND)
			.message("존재하지 않는 자원입니다.")
			.build();
		response.setStatus(404);
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
