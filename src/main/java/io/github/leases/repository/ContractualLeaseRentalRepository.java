package io.github.leases.repository;

import io.github.leases.domain.ContractualLeaseRental;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContractualLeaseRental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractualLeaseRentalRepository extends JpaRepository<ContractualLeaseRental, Long>, JpaSpecificationExecutor<ContractualLeaseRental> {
}
