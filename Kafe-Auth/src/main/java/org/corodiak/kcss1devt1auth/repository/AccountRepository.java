package org.corodiak.kcss1devt1auth.repository;

import java.util.Optional;

import org.corodiak.kcss1devt1auth.type.entity.Account;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUser(User user);

	Optional<Account> findByEmail(String email);
}
