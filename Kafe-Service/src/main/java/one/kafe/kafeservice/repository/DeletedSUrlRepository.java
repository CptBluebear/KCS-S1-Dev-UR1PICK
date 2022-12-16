package one.kafe.kafeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import one.kafe.kafeservice.repository.qrepository.QDeletedSUrlRepository;
import one.kafe.kafeservice.type.entity.DeletedSurl;

public interface DeletedSUrlRepository extends JpaRepository<DeletedSurl, Long>, QDeletedSUrlRepository {
}
