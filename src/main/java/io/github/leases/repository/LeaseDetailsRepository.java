package io.github.leases.repository;

import io.github.leases.domain.LeaseDetails;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeaseDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaseDetailsRepository extends JpaRepository<LeaseDetails, Long>, JpaSpecificationExecutor<LeaseDetails> {
}
