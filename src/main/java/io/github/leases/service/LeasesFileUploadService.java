package io.github.leases.service;

import io.github.leases.service.dto.LeasesFileUploadDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.leases.domain.LeasesFileUpload}.
 */
public interface LeasesFileUploadService {

    /**
     * Save a leasesFileUpload.
     *
     * @param leasesFileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    LeasesFileUploadDTO save(LeasesFileUploadDTO leasesFileUploadDTO);

    /**
     * Get all the leasesFileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasesFileUploadDTO> findAll(Pageable pageable);


    /**
     * Get the "id" leasesFileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeasesFileUploadDTO> findOne(Long id);

    /**
     * Delete the "id" leasesFileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leasesFileUpload corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasesFileUploadDTO> search(String query, Pageable pageable);
}
