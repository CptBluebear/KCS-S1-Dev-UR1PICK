package one.kafe.kafeservice.repository.qrepository;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.Tuple;

import one.kafe.kafeservice.type.entity.SUrl;

public interface QSUrlRepository {
	List<SUrl> findByUserSeq(Long userSeq);

	Optional<SUrl> findByIdWithUser(Long seq);

	List<Tuple> findShortUrlByUserSeq(Long userSeq);
}
