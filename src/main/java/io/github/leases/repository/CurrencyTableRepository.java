package io.github.leases.repository;

import io.github.leases.domain.CurrencyTable;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CurrencyTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyTableRepository extends JpaRepository<CurrencyTable, Long>, JpaSpecificationExecutor<CurrencyTable> {
}
