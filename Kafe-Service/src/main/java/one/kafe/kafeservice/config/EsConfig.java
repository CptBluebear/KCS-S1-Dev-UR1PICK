package one.kafe.kafeservice.config;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableElasticsearchRepositories(basePackages = "one.kafe.kafeservice.repository")
@RequiredArgsConstructor
public class EsConfig extends AbstractElasticsearchConfiguration {

	/**
	 * Elasticsearch Connect 관련 설정
	 */
	private final EsProperties esProperties;

	@Bean
	@Override
	public RestHighLevelClient elasticsearchClient() {
		RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(esProperties.httpHost()));
		return client;
	}

}