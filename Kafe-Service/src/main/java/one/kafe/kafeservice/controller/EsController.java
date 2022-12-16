package one.kafe.kafeservice.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.auth.util.AuthUtil;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.service.DocumentService;
import one.kafe.kafeservice.type.dto.ResponseModel;

@RestController
@RequiredArgsConstructor
public class EsController {

	private final DocumentService documentService;

	@GetMapping("/api/statistics/{seq}")
	public ResponseModel statistics(@PathVariable Long seq) throws
		PermissionDeniedException,
		SearchResultNotExistException,
		IOException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		ResponseModel responseModel = ResponseModel.builder().build();
		responseModel.addData("statistics", documentService.statistic(seq, userSeq));
		return responseModel;
	}

}
