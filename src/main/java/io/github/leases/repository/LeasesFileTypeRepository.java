package io.github.leases.repository;

import io.github.leases.domain.LeasesFileType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeasesFileType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeasesFileTypeRepository extends JpaRepository<LeasesFileType, Long>, JpaSpecificationExecutor<LeasesFileType> {
}
