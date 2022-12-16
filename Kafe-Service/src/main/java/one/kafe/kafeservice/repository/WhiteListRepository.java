package one.kafe.kafeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.kafe.kafeservice.repository.qrepository.QWhiteListRepository;
import one.kafe.kafeservice.type.entity.WhiteList;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList, Long>, QWhiteListRepository {
	boolean existsByDomain(String domain);
}
