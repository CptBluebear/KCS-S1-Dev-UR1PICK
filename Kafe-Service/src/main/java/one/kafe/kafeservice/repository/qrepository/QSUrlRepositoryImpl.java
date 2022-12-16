package one.kafe.kafeservice.repository.qrepository;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.type.entity.QSUrl;
import one.kafe.kafeservice.type.entity.QUser;
import one.kafe.kafeservice.type.entity.SUrl;

@RequiredArgsConstructor
public class QSUrlRepositoryImpl implements QSUrlRepository {

	private final JPAQueryFactory queryFactory;

	private QSUrl qsUrl = QSUrl.sUrl;
	private QUser qUser = QUser.user;

	public List<SUrl> findByUserSeq(Long userSeq) {
		return queryFactory.selectFrom(qsUrl)
			.where(qsUrl.user.seq.eq(userSeq))
			.fetch();
	}

	@Override
	public Optional<SUrl> findByIdWithUser(Long seq) {
		return Optional.ofNullable(queryFactory.selectFrom(qsUrl)
			.where(qsUrl.seq.eq(seq))
			.innerJoin(qsUrl.user, qUser)
			.fetchJoin()
			.fetchOne());
	}

	public List<Tuple> findShortUrlByUserSeq(Long userSeq) {
		return queryFactory.select(qsUrl.shortUrl, qsUrl.accessCount, qsUrl.registerDate)
			.from(qsUrl)
			.where(qsUrl.user.seq.eq(userSeq))
			.fetch();
	}

}
