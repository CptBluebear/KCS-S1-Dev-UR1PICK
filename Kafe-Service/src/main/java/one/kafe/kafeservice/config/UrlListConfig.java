package one.kafe.kafeservice.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import one.kafe.kafeservice.util.UrlList;

@Configuration
public class UrlListConfig {

	@Value("${url-filter.blacklist.path}")
	private String blackListPath;

	@Bean
	public UrlList urlList() throws IOException {
		return new UrlList(blackListPath);
	}
}
