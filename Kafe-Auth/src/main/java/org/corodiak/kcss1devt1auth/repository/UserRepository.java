package org.corodiak.kcss1devt1auth.repository;

import org.corodiak.kcss1devt1auth.repository.qrepository.QUserRepository;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QUserRepository {
}
