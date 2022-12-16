package one.kafe.kafeurlredirect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.kafe.kafeurlredirect.repository.qrepository.QSUrlRepository;
import one.kafe.kafeurlredirect.type.entity.SUrl;

@Repository
public interface SUrlRepository extends JpaRepository<SUrl, Long>, QSUrlRepository {

	Optional<SUrl> findByShortUrl(String shortUrl);

}
