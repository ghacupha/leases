package io.github.leases.repository;

import io.github.leases.domain.LeasesMessageToken;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeasesMessageToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeasesMessageTokenRepository extends JpaRepository<LeasesMessageToken, Long>, JpaSpecificationExecutor<LeasesMessageToken> {
}
