package one.kafe.kafeurlredirect.type.etc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum URLStatus {
	NORMAL("Normal URL"),
	MALICIOUS_BLACKLIST("Malicious URL - Blacklist"),
	MALICIOUS_CERT_NOT_VALID("Malicious URL - Certification"),
	MALICIOUS_URL_SIMILAR("Malicious URL - URL is too Similar"),
	INSPECTING("URL still Checking");

	private final String description;
}
