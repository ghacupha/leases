package io.github.leases.service.impl;

import io.github.leases.service.ContractualLeaseRentalService;
import io.github.leases.domain.ContractualLeaseRental;
import io.github.leases.repository.ContractualLeaseRentalRepository;
import io.github.leases.repository.search.ContractualLeaseRentalSearchRepository;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import io.github.leases.service.mapper.ContractualLeaseRentalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ContractualLeaseRental}.
 */
@Service
@Transactional
public class ContractualLeaseRentalServiceImpl implements ContractualLeaseRentalService {

    private final Logger log = LoggerFactory.getLogger(ContractualLeaseRentalServiceImpl.class);

    private final ContractualLeaseRentalRepository contractualLeaseRentalRepository;

    private final ContractualLeaseRentalMapper contractualLeaseRentalMapper;

    private final ContractualLeaseRentalSearchRepository contractualLeaseRentalSearchRepository;

    public ContractualLeaseRentalServiceImpl(ContractualLeaseRentalRepository contractualLeaseRentalRepository, ContractualLeaseRentalMapper contractualLeaseRentalMapper, ContractualLeaseRentalSearchRepository contractualLeaseRentalSearchRepository) {
        this.contractualLeaseRentalRepository = contractualLeaseRentalRepository;
        this.contractualLeaseRentalMapper = contractualLeaseRentalMapper;
        this.contractualLeaseRentalSearchRepository = contractualLeaseRentalSearchRepository;
    }

    @Override
    public ContractualLeaseRentalDTO save(ContractualLeaseRentalDTO contractualLeaseRentalDTO) {
        log.debug("Request to save ContractualLeaseRental : {}", contractualLeaseRentalDTO);
        ContractualLeaseRental contractualLeaseRental = contractualLeaseRentalMapper.toEntity(contractualLeaseRentalDTO);
        contractualLeaseRental = contractualLeaseRentalRepository.save(contractualLeaseRental);
        ContractualLeaseRentalDTO result = contractualLeaseRentalMapper.toDto(contractualLeaseRental);
        contractualLeaseRentalSearchRepository.save(contractualLeaseRental);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractualLeaseRentalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContractualLeaseRentals");
        return contractualLeaseRentalRepository.findAll(pageable)
            .map(contractualLeaseRentalMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ContractualLeaseRentalDTO> findOne(Long id) {
        log.debug("Request to get ContractualLeaseRental : {}", id);
        return contractualLeaseRentalRepository.findById(id)
            .map(contractualLeaseRentalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContractualLeaseRental : {}", id);
        contractualLeaseRentalRepository.deleteById(id);
        contractualLeaseRentalSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractualLeaseRentalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContractualLeaseRentals for query {}", query);
        return contractualLeaseRentalSearchRepository.search(queryStringQuery(query), pageable)
            .map(contractualLeaseRentalMapper::toDto);
    }
}
