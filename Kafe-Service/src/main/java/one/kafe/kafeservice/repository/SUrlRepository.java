package one.kafe.kafeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.kafe.kafeservice.repository.qrepository.QSUrlRepository;
import one.kafe.kafeservice.type.entity.SUrl;

@Repository
public interface SUrlRepository extends JpaRepository<SUrl, Long>, QSUrlRepository {
	boolean existsByShortUrl(String shortUrl);
}
