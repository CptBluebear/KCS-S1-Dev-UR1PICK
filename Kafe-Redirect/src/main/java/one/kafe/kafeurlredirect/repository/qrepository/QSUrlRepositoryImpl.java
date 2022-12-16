package one.kafe.kafeurlredirect.repository.qrepository;

import java.time.LocalDateTime;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeurlredirect.type.entity.QSUrl;

@RequiredArgsConstructor
public class QSUrlRepositoryImpl implements QSUrlRepository {

	private final JPAQueryFactory queryFactory;

	private QSUrl qsUrl = QSUrl.sUrl;

	public void updateAccessCountAndLastAccess(Long seq) {
		queryFactory.update(qsUrl)
			.set(qsUrl.accessCount, qsUrl.accessCount.add(1))
			.set(qsUrl.lastAccess, LocalDateTime.now())
			.where(qsUrl.seq.eq(seq))
			.execute();
	}

}
