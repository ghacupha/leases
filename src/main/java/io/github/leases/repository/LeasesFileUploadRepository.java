package io.github.leases.repository;

import io.github.leases.domain.LeasesFileUpload;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the LeasesFileUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeasesFileUploadRepository extends JpaRepository<LeasesFileUpload, Long>, JpaSpecificationExecutor<LeasesFileUpload> {
}
