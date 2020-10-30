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

import io.github.leases.domain.LeaseDetails;
import io.github.leases.domain.*; // for static metamodels
import io.github.leases.repository.LeaseDetailsRepository;
import io.github.leases.repository.search.LeaseDetailsSearchRepository;
import io.github.leases.service.dto.LeaseDetailsCriteria;
import io.github.leases.service.dto.LeaseDetailsDTO;
import io.github.leases.service.mapper.LeaseDetailsMapper;

/**
 * Service for executing complex queries for {@link LeaseDetails} entities in the database.
 * The main input is a {@link LeaseDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseDetailsDTO} or a {@link Page} of {@link LeaseDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseDetailsQueryService extends QueryService<LeaseDetails> {

    private final Logger log = LoggerFactory.getLogger(LeaseDetailsQueryService.class);

    private final LeaseDetailsRepository leaseDetailsRepository;

    private final LeaseDetailsMapper leaseDetailsMapper;

    private final LeaseDetailsSearchRepository leaseDetailsSearchRepository;

    public LeaseDetailsQueryService(LeaseDetailsRepository leaseDetailsRepository, LeaseDetailsMapper leaseDetailsMapper, LeaseDetailsSearchRepository leaseDetailsSearchRepository) {
        this.leaseDetailsRepository = leaseDetailsRepository;
        this.leaseDetailsMapper = leaseDetailsMapper;
        this.leaseDetailsSearchRepository = leaseDetailsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseDetailsDTO> findByCriteria(LeaseDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseDetails> specification = createSpecification(criteria);
        return leaseDetailsMapper.toDto(leaseDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseDetailsDTO> findByCriteria(LeaseDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseDetails> specification = createSpecification(criteria);
        return leaseDetailsRepository.findAll(specification, page)
            .map(leaseDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseDetails> specification = createSpecification(criteria);
        return leaseDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseDetails> createSpecification(LeaseDetailsCriteria criteria) {
        Specification<LeaseDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseDetails_.id));
            }
            if (criteria.getLeaseContractNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseContractNumber(), LeaseDetails_.leaseContractNumber));
            }
            if (criteria.getIncrementalBorrowingRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIncrementalBorrowingRate(), LeaseDetails_.incrementalBorrowingRate));
            }
            if (criteria.getCommencementDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommencementDate(), LeaseDetails_.commencementDate));
            }
            if (criteria.getLeasePrepayments() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeasePrepayments(), LeaseDetails_.leasePrepayments));
            }
            if (criteria.getInitialDirectCosts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInitialDirectCosts(), LeaseDetails_.initialDirectCosts));
            }
            if (criteria.getDemolitionCosts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDemolitionCosts(), LeaseDetails_.demolitionCosts));
            }
            if (criteria.getAssetAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetAccountNumber(), LeaseDetails_.assetAccountNumber));
            }
            if (criteria.getLiabilityAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLiabilityAccountNumber(), LeaseDetails_.liabilityAccountNumber));
            }
            if (criteria.getDepreciationAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepreciationAccountNumber(), LeaseDetails_.depreciationAccountNumber));
            }
            if (criteria.getInterestAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInterestAccountNumber(), LeaseDetails_.interestAccountNumber));
            }
        }
        return specification;
    }
}
