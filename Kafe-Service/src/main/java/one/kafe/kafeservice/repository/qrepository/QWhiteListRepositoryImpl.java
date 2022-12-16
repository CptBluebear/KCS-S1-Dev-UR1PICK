package one.kafe.kafeservice.repository.qrepository;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import one.kafe.kafeservice.type.entity.QWhiteList;

@RequiredArgsConstructor
public class QWhiteListRepositoryImpl implements QWhiteListRepository {

	private final JPAQueryFactory queryFactory;

	private final QWhiteList qWhiteList = QWhiteList.whiteList;

	public List<String> getWhiteListDomain() {
		return queryFactory.select(qWhiteList.domain)
			.from(qWhiteList)
			.fetch();
	}

}
