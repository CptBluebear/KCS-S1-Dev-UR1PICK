package org.corodiak.kcss1devt1auth.repository;

import org.corodiak.kcss1devt1auth.repository.qrepository.QEmailVerificationRepository;
import org.corodiak.kcss1devt1auth.type.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long>,
	QEmailVerificationRepository {

	EmailVerification findByCode(String code);

}
