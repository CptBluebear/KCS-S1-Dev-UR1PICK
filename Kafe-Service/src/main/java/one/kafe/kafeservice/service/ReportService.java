package one.kafe.kafeservice.service;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ReportService {
	void generateReportBase(Long userSeq, LocalDateTime base) throws IOException;
}
