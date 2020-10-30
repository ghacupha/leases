package io.github.leases.service;

import io.github.leases.service.dto.ContractualLeaseRentalDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.leases.domain.ContractualLeaseRental}.
 */
public interface ContractualLeaseRentalService {

    /**
     * Save a contractualLeaseRental.
     *
     * @param contractualLeaseRentalDTO the entity to save.
     * @return the persisted entity.
     */
    ContractualLeaseRentalDTO save(ContractualLeaseRentalDTO contractualLeaseRentalDTO);

    /**
     * Get all the contractualLeaseRentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractualLeaseRentalDTO> findAll(Pageable pageable);


    /**
     * Get the "id" contractualLeaseRental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractualLeaseRentalDTO> findOne(Long id);

    /**
     * Delete the "id" contractualLeaseRental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the contractualLeaseRental corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractualLeaseRentalDTO> search(String query, Pageable pageable);
}
