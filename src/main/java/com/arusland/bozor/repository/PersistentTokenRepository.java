package com.arusland.bozor.repository;

import com.arusland.bozor.domain.PersistentToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the PersistentToken entity.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(String userName);

    List<PersistentToken> findByTokenDateBefore(Date localDate);
}
