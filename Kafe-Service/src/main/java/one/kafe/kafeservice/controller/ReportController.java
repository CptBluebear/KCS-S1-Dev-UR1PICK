package one.kafe.kafeservice.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.auth.util.AuthUtil;
import one.kafe.kafeservice.service.ReportService;
import one.kafe.kafeservice.type.dto.ResponseModel;

@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@RequestMapping(value = "/api/statistics/report", method = RequestMethod.GET)
	public ResponseModel generateReport() throws IOException {
		Long userSeq = AuthUtil.getAuthenticationInfoSeq();
		reportService.generateReportBase(userSeq, LocalDateTime.now());
		return ResponseModel.builder().build();
	}

}
