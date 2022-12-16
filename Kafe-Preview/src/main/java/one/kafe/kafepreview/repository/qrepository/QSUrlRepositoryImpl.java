package one.kafe.kafepreview.repository.qrepository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import one.kafe.kafepreview.type.entity.QSUrl;

@RequiredArgsConstructor
public class QSUrlRepositoryImpl implements QSUrlRepository {

	private final JPAQueryFactory queryFactory;

	private QSUrl qsUrl = QSUrl.sUrl;

	public boolean updatePreviewBySeq(Long seq, String url) {
		return queryFactory.update(qsUrl)
			.set(qsUrl.preview, url)
			.where(qsUrl.seq.eq(seq))
			.execute() > 0;
	}
}
