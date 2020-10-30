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

import io.github.leases.domain.LeasesFileUpload;
import io.github.leases.domain.*; // for static metamodels
import io.github.leases.repository.LeasesFileUploadRepository;
import io.github.leases.repository.search.LeasesFileUploadSearchRepository;
import io.github.leases.service.dto.LeasesFileUploadCriteria;
import io.github.leases.service.dto.LeasesFileUploadDTO;
import io.github.leases.service.mapper.LeasesFileUploadMapper;

/**
 * Service for executing complex queries for {@link LeasesFileUpload} entities in the database.
 * The main input is a {@link LeasesFileUploadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeasesFileUploadDTO} or a {@link Page} of {@link LeasesFileUploadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeasesFileUploadQueryService extends QueryService<LeasesFileUpload> {

    private final Logger log = LoggerFactory.getLogger(LeasesFileUploadQueryService.class);

    private final LeasesFileUploadRepository leasesFileUploadRepository;

    private final LeasesFileUploadMapper leasesFileUploadMapper;

    private final LeasesFileUploadSearchRepository leasesFileUploadSearchRepository;

    public LeasesFileUploadQueryService(LeasesFileUploadRepository leasesFileUploadRepository, LeasesFileUploadMapper leasesFileUploadMapper, LeasesFileUploadSearchRepository leasesFileUploadSearchRepository) {
        this.leasesFileUploadRepository = leasesFileUploadRepository;
        this.leasesFileUploadMapper = leasesFileUploadMapper;
        this.leasesFileUploadSearchRepository = leasesFileUploadSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeasesFileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeasesFileUploadDTO> findByCriteria(LeasesFileUploadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeasesFileUpload> specification = createSpecification(criteria);
        return leasesFileUploadMapper.toDto(leasesFileUploadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeasesFileUploadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasesFileUploadDTO> findByCriteria(LeasesFileUploadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeasesFileUpload> specification = createSpecification(criteria);
        return leasesFileUploadRepository.findAll(specification, page)
            .map(leasesFileUploadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeasesFileUploadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeasesFileUpload> specification = createSpecification(criteria);
        return leasesFileUploadRepository.count(specification);
    }

    /**
     * Function to convert {@link LeasesFileUploadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeasesFileUpload> createSpecification(LeasesFileUploadCriteria criteria) {
        Specification<LeasesFileUpload> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeasesFileUpload_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeasesFileUpload_.description));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), LeasesFileUpload_.fileName));
            }
            if (criteria.getPeriodFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodFrom(), LeasesFileUpload_.periodFrom));
            }
            if (criteria.getPeriodTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPeriodTo(), LeasesFileUpload_.periodTo));
            }
            if (criteria.getLeasesFileTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeasesFileTypeId(), LeasesFileUpload_.leasesFileTypeId));
            }
            if (criteria.getUploadSuccessful() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadSuccessful(), LeasesFileUpload_.uploadSuccessful));
            }
            if (criteria.getUploadProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getUploadProcessed(), LeasesFileUpload_.uploadProcessed));
            }
            if (criteria.getUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUploadToken(), LeasesFileUpload_.uploadToken));
            }
        }
        return specification;
    }
}
