package one.kafe.kafepreview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import one.kafe.kafepreview.repository.qrepository.QSUrlRepository;
import one.kafe.kafepreview.type.entity.SUrl;

public interface SUrlRepository extends JpaRepository<SUrl, Long>, QSUrlRepository {
}
