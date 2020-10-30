package io.github.leases.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.leases.domain.LeasesFileType;
import io.github.leases.domain.*; // for static metamodels
import io.github.leases.repository.LeasesFileTypeRepository;
import io.github.leases.repository.search.LeasesFileTypeSearchRepository;
import io.github.leases.service.dto.LeasesFileTypeCriteria;

/**
 * Service for executing complex queries for {@link LeasesFileType} entities in the database.
 * The main input is a {@link LeasesFileTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeasesFileType} or a {@link Page} of {@link LeasesFileType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeasesFileTypeQueryService extends QueryService<LeasesFileType> {

    private final Logger log = LoggerFactory.getLogger(LeasesFileTypeQueryService.class);

    private final LeasesFileTypeRepository leasesFileTypeRepository;

    private final LeasesFileTypeSearchRepository leasesFileTypeSearchRepository;

    public LeasesFileTypeQueryService(LeasesFileTypeRepository leasesFileTypeRepository, LeasesFileTypeSearchRepository leasesFileTypeSearchRepository) {
        this.leasesFileTypeRepository = leasesFileTypeRepository;
        this.leasesFileTypeSearchRepository = leasesFileTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeasesFileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeasesFileType> findByCriteria(LeasesFileTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeasesFileType> specification = createSpecification(criteria);
        return leasesFileTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LeasesFileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasesFileType> findByCriteria(LeasesFileTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeasesFileType> specification = createSpecification(criteria);
        return leasesFileTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeasesFileTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeasesFileType> specification = createSpecification(criteria);
        return leasesFileTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link LeasesFileTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeasesFileType> createSpecification(LeasesFileTypeCriteria criteria) {
        Specification<LeasesFileType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeasesFileType_.id));
            }
            if (criteria.getLeasesFileTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeasesFileTypeName(), LeasesFileType_.leasesFileTypeName));
            }
            if (criteria.getLeasesFileMediumType() != null) {
                specification = specification.and(buildSpecification(criteria.getLeasesFileMediumType(), LeasesFileType_.leasesFileMediumType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeasesFileType_.description));
            }
            if (criteria.getLeasesfileType() != null) {
                specification = specification.and(buildSpecification(criteria.getLeasesfileType(), LeasesFileType_.leasesfileType));
            }
        }
        return specification;
    }
}
