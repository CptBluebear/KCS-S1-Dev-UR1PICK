package one.kafe.kafeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.kafe.kafeservice.type.entity.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

	boolean existsByUrl(String url);

}
