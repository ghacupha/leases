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

import io.github.leases.domain.ContractualLeaseRental;
import io.github.leases.domain.*; // for static metamodels
import io.github.leases.repository.ContractualLeaseRentalRepository;
import io.github.leases.repository.search.ContractualLeaseRentalSearchRepository;
import io.github.leases.service.dto.ContractualLeaseRentalCriteria;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import io.github.leases.service.mapper.ContractualLeaseRentalMapper;

/**
 * Service for executing complex queries for {@link ContractualLeaseRental} entities in the database.
 * The main input is a {@link ContractualLeaseRentalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractualLeaseRentalDTO} or a {@link Page} of {@link ContractualLeaseRentalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractualLeaseRentalQueryService extends QueryService<ContractualLeaseRental> {

    private final Logger log = LoggerFactory.getLogger(ContractualLeaseRentalQueryService.class);

    private final ContractualLeaseRentalRepository contractualLeaseRentalRepository;

    private final ContractualLeaseRentalMapper contractualLeaseRentalMapper;

    private final ContractualLeaseRentalSearchRepository contractualLeaseRentalSearchRepository;

    public ContractualLeaseRentalQueryService(ContractualLeaseRentalRepository contractualLeaseRentalRepository, ContractualLeaseRentalMapper contractualLeaseRentalMapper, ContractualLeaseRentalSearchRepository contractualLeaseRentalSearchRepository) {
        this.contractualLeaseRentalRepository = contractualLeaseRentalRepository;
        this.contractualLeaseRentalMapper = contractualLeaseRentalMapper;
        this.contractualLeaseRentalSearchRepository = contractualLeaseRentalSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContractualLeaseRentalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractualLeaseRentalDTO> findByCriteria(ContractualLeaseRentalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContractualLeaseRental> specification = createSpecification(criteria);
        return contractualLeaseRentalMapper.toDto(contractualLeaseRentalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContractualLeaseRentalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractualLeaseRentalDTO> findByCriteria(ContractualLeaseRentalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContractualLeaseRental> specification = createSpecification(criteria);
        return contractualLeaseRentalRepository.findAll(specification, page)
            .map(contractualLeaseRentalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractualLeaseRentalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContractualLeaseRental> specification = createSpecification(criteria);
        return contractualLeaseRentalRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractualLeaseRentalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContractualLeaseRental> createSpecification(ContractualLeaseRentalCriteria criteria) {
        Specification<ContractualLeaseRental> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContractualLeaseRental_.id));
            }
            if (criteria.getLeaseContractNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseContractNumber(), ContractualLeaseRental_.leaseContractNumber));
            }
            if (criteria.getRentalSequenceNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRentalSequenceNumber(), ContractualLeaseRental_.rentalSequenceNumber));
            }
            if (criteria.getLeaseRentalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaseRentalDate(), ContractualLeaseRental_.leaseRentalDate));
            }
            if (criteria.getLeaseRentalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaseRentalAmount(), ContractualLeaseRental_.leaseRentalAmount));
            }
        }
        return specification;
    }
}
