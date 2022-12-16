package one.kafe.kafeservice.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.CountQuery;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import one.kafe.kafeservice.type.entity.EsIndex;

public interface EsIndexRepository extends ElasticsearchRepository<EsIndex, String> {
	@Query("{\"match\": {\"current_path.keyword\" : \"?0\"}}")
	List<EsIndex> findByUrl(String url);

	@CountQuery("{\"match\": {\"current_path.keyword\" : \"?0\"}}")
	Long getUrlCnt(String url);
}
