package io.github.leases.service;

import io.github.leases.domain.LeasesFileType;
import io.github.leases.repository.LeasesFileTypeRepository;
import io.github.leases.repository.search.LeasesFileTypeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link LeasesFileType}.
 */
@Service
@Transactional
public class LeasesFileTypeService {

    private final Logger log = LoggerFactory.getLogger(LeasesFileTypeService.class);

    private final LeasesFileTypeRepository leasesFileTypeRepository;

    private final LeasesFileTypeSearchRepository leasesFileTypeSearchRepository;

    public LeasesFileTypeService(LeasesFileTypeRepository leasesFileTypeRepository, LeasesFileTypeSearchRepository leasesFileTypeSearchRepository) {
        this.leasesFileTypeRepository = leasesFileTypeRepository;
        this.leasesFileTypeSearchRepository = leasesFileTypeSearchRepository;
    }

    /**
     * Save a leasesFileType.
     *
     * @param leasesFileType the entity to save.
     * @return the persisted entity.
     */
    public LeasesFileType save(LeasesFileType leasesFileType) {
        log.debug("Request to save LeasesFileType : {}", leasesFileType);
        LeasesFileType result = leasesFileTypeRepository.save(leasesFileType);
        leasesFileTypeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the leasesFileTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasesFileType> findAll(Pageable pageable) {
        log.debug("Request to get all LeasesFileTypes");
        return leasesFileTypeRepository.findAll(pageable);
    }


    /**
     * Get one leasesFileType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeasesFileType> findOne(Long id) {
        log.debug("Request to get LeasesFileType : {}", id);
        return leasesFileTypeRepository.findById(id);
    }

    /**
     * Delete the leasesFileType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeasesFileType : {}", id);
        leasesFileTypeRepository.deleteById(id);
        leasesFileTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the leasesFileType corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasesFileType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasesFileTypes for query {}", query);
        return leasesFileTypeSearchRepository.search(queryStringQuery(query), pageable);    }
}
