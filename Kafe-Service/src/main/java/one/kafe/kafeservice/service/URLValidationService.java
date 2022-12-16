package one.kafe.kafeservice.service;

import one.kafe.kafeservice.type.etc.URLStatus;

public interface URLValidationService {
	URLStatus validURL(String url);
}
