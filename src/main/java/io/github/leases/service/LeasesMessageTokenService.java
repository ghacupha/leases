package io.github.leases.service;

import io.github.leases.service.dto.LeasesMessageTokenDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.github.leases.domain.LeasesMessageToken}.
 */
public interface LeasesMessageTokenService {

    /**
     * Save a leasesMessageToken.
     *
     * @param leasesMessageTokenDTO the entity to save.
     * @return the persisted entity.
     */
    LeasesMessageTokenDTO save(LeasesMessageTokenDTO leasesMessageTokenDTO);

    /**
     * Get all the leasesMessageTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasesMessageTokenDTO> findAll(Pageable pageable);


    /**
     * Get the "id" leasesMessageToken.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeasesMessageTokenDTO> findOne(Long id);

    /**
     * Delete the "id" leasesMessageToken.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the leasesMessageToken corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeasesMessageTokenDTO> search(String query, Pageable pageable);
}
