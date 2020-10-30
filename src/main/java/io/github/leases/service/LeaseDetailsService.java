package io.github.leases.service;

import io.github.leases.service.dto.LeaseDetailsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.leases.domain.LeaseDetails}.
 */
public interface LeaseDetailsService {

    /**
     * Save a leaseDetails.
     *
     * @param leaseDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    LeaseDetailsDTO save(LeaseDetailsDTO leaseDetailsDTO);

    /**
     * Get all the leaseDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseDetailsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" leaseDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaseDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" leaseDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leaseDetails corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaseDetailsDTO> search(String query, Pageable pageable);
}
