package one.kafe.kafeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import one.kafe.kafeservice.repository.qrepository.QUserRepository;
import one.kafe.kafeservice.type.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QUserRepository {
}
