package org.corodiak.kcss1devt1auth.repository;

import java.util.Optional;

import org.corodiak.kcss1devt1auth.type.entity.OAuthUser;
import org.corodiak.kcss1devt1auth.type.etc.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {

	Optional<OAuthUser> findByProviderUserIdAndOap(String providerUserId, OAuthProvider oap);

}
