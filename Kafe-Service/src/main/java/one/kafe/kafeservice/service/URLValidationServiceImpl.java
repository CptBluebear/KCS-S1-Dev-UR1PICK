package one.kafe.kafeservice.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.repository.BlackListRepository;
import one.kafe.kafeservice.repository.WhiteListRepository;
import one.kafe.kafeservice.type.etc.URLStatus;
import one.kafe.kafeservice.util.UrlList;

@Service
@RequiredArgsConstructor
public class URLValidationServiceImpl implements URLValidationService {

	private final UrlList urlList;
	private final BlackListRepository blackListRepository;
	private final WhiteListRepository whiteListRepository;

	private final Pattern pattern = Pattern.compile(
		"^(https?):\\/\\/((?:www\\.)?([-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}))\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$");

	@Override
	public URLStatus validURL(String url) {
		if(url.contains("#")) {
			url = url.substring(0, url.lastIndexOf('#'));
		}
		if(url.charAt(url.length()-1) == '/') {
			url = url.substring(0, url.length()-1);
		}
		if (blackListRepository.existsByUrl(url) || urlList.containBlacklist(url)) {
			return URLStatus.MALICIOUS_BLACKLIST;
		}
		Matcher matcher = pattern.matcher(url);
		matcher.matches();
		String protocol = matcher.group(1);
		String domain = matcher.group(3);
		if(domain.equals("kafe.one")) {
			return URLStatus.LOOP_REDIRECT;
		}
		if (whiteListRepository.existsByDomain(domain)) {
			return URLStatus.NORMAL;
		}
		try {
			switch (protocol) {
				case "http":
					break;
				case "https":
					URL target = new URL(url);
					HttpsURLConnection connection = (HttpsURLConnection)target.openConnection();
					connection.connect();
					connection.disconnect();
					break;
			}
		} catch (MalformedURLException syntaxException) {
			// 잘못된 주소 양식
			return URLStatus.NORMAL;
		} catch (SSLHandshakeException sslHandshakeException) {
			// 잘못된 인증서
			return URLStatus.MALICIOUS_CERT_NOT_VALID;
		} catch (IOException ioException) {
			// 연결 에러
			return URLStatus.NORMAL;
		}
		List<String> whiteList = whiteListRepository.getWhiteListDomain();
		double similarity = calculateDomainSimilarity(domain, whiteList);
		if (similarity != 0.0 && similarity < 0.15) {
			return URLStatus.MALICIOUS_URL_SIMILAR;
		}
		return URLStatus.NORMAL;
	}

	private double calculateDomainSimilarity(String domain, List<String> whiteList) {
		LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();
		double result = 1.0;
		for (String white : whiteList) {
			result = Math.min((double)(levenshteinDistance.apply(domain, white)) / (double)(white.length()), result);
		}
		return result;
	}
}