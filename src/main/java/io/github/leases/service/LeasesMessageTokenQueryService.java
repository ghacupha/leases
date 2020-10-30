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

import io.github.leases.domain.LeasesMessageToken;
import io.github.leases.domain.*; // for static metamodels
import io.github.leases.repository.LeasesMessageTokenRepository;
import io.github.leases.repository.search.LeasesMessageTokenSearchRepository;
import io.github.leases.service.dto.LeasesMessageTokenCriteria;
import io.github.leases.service.dto.LeasesMessageTokenDTO;
import io.github.leases.service.mapper.LeasesMessageTokenMapper;

/**
 * Service for executing complex queries for {@link LeasesMessageToken} entities in the database.
 * The main input is a {@link LeasesMessageTokenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeasesMessageTokenDTO} or a {@link Page} of {@link LeasesMessageTokenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeasesMessageTokenQueryService extends QueryService<LeasesMessageToken> {

    private final Logger log = LoggerFactory.getLogger(LeasesMessageTokenQueryService.class);

    private final LeasesMessageTokenRepository leasesMessageTokenRepository;

    private final LeasesMessageTokenMapper leasesMessageTokenMapper;

    private final LeasesMessageTokenSearchRepository leasesMessageTokenSearchRepository;

    public LeasesMessageTokenQueryService(LeasesMessageTokenRepository leasesMessageTokenRepository, LeasesMessageTokenMapper leasesMessageTokenMapper, LeasesMessageTokenSearchRepository leasesMessageTokenSearchRepository) {
        this.leasesMessageTokenRepository = leasesMessageTokenRepository;
        this.leasesMessageTokenMapper = leasesMessageTokenMapper;
        this.leasesMessageTokenSearchRepository = leasesMessageTokenSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeasesMessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeasesMessageTokenDTO> findByCriteria(LeasesMessageTokenCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeasesMessageToken> specification = createSpecification(criteria);
        return leasesMessageTokenMapper.toDto(leasesMessageTokenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeasesMessageTokenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasesMessageTokenDTO> findByCriteria(LeasesMessageTokenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeasesMessageToken> specification = createSpecification(criteria);
        return leasesMessageTokenRepository.findAll(specification, page)
            .map(leasesMessageTokenMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeasesMessageTokenCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeasesMessageToken> specification = createSpecification(criteria);
        return leasesMessageTokenRepository.count(specification);
    }

    /**
     * Function to convert {@link LeasesMessageTokenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeasesMessageToken> createSpecification(LeasesMessageTokenCriteria criteria) {
        Specification<LeasesMessageToken> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeasesMessageToken_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeasesMessageToken_.description));
            }
            if (criteria.getTimeSent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeSent(), LeasesMessageToken_.timeSent));
            }
            if (criteria.getTokenValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTokenValue(), LeasesMessageToken_.tokenValue));
            }
            if (criteria.getReceived() != null) {
                specification = specification.and(buildSpecification(criteria.getReceived(), LeasesMessageToken_.received));
            }
            if (criteria.getActioned() != null) {
                specification = specification.and(buildSpecification(criteria.getActioned(), LeasesMessageToken_.actioned));
            }
            if (criteria.getContentFullyEnqueued() != null) {
                specification = specification.and(buildSpecification(criteria.getContentFullyEnqueued(), LeasesMessageToken_.contentFullyEnqueued));
            }
        }
        return specification;
    }
}
