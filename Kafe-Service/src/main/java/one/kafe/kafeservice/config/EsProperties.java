package one.kafe.kafeservice.config;

import java.util.Optional;

import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Configuration
@ConfigurationProperties("elasticsearch")
public class EsProperties {

	/**
	 * Elasticsearch 연결시에 필요한 데이터들을 설정
	 * ConfigurationProperties : application.yml 파일에 있는 값과 매칭시킴
	 */
	private String host; // ES Host

	private int port; // ES Port

	private Indices indices; // ES Index 정보

	@Bean
	public HttpHost httpHost() {
		return new HttpHost(host, port, "http");
	}

	@Bean
	public String getWebIndexName() {
		return Optional.ofNullable(indices).map(Indices::getWebIndexName).orElse(null);
	}

	@Getter
	@Setter
	public static class Indices {
		String webIndexName; // index
	}
}