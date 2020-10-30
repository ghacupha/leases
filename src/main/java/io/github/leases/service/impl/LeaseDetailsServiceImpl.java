package io.github.leases.service.impl;

import io.github.leases.service.LeaseDetailsService;
import io.github.leases.domain.LeaseDetails;
import io.github.leases.repository.LeaseDetailsRepository;
import io.github.leases.repository.search.LeaseDetailsSearchRepository;
import io.github.leases.service.dto.LeaseDetailsDTO;
import io.github.leases.service.mapper.LeaseDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link LeaseDetails}.
 */
@Service
@Transactional
public class LeaseDetailsServiceImpl implements LeaseDetailsService {

    private final Logger log = LoggerFactory.getLogger(LeaseDetailsServiceImpl.class);

    private final LeaseDetailsRepository leaseDetailsRepository;

    private final LeaseDetailsMapper leaseDetailsMapper;

    private final LeaseDetailsSearchRepository leaseDetailsSearchRepository;

    public LeaseDetailsServiceImpl(LeaseDetailsRepository leaseDetailsRepository, LeaseDetailsMapper leaseDetailsMapper, LeaseDetailsSearchRepository leaseDetailsSearchRepository) {
        this.leaseDetailsRepository = leaseDetailsRepository;
        this.leaseDetailsMapper = leaseDetailsMapper;
        this.leaseDetailsSearchRepository = leaseDetailsSearchRepository;
    }

    @Override
    public LeaseDetailsDTO save(LeaseDetailsDTO leaseDetailsDTO) {
        log.debug("Request to save LeaseDetails : {}", leaseDetailsDTO);
        LeaseDetails leaseDetails = leaseDetailsMapper.toEntity(leaseDetailsDTO);
        leaseDetails = leaseDetailsRepository.save(leaseDetails);
        LeaseDetailsDTO result = leaseDetailsMapper.toDto(leaseDetails);
        leaseDetailsSearchRepository.save(leaseDetails);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaseDetails");
        return leaseDetailsRepository.findAll(pageable)
            .map(leaseDetailsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<LeaseDetailsDTO> findOne(Long id) {
        log.debug("Request to get LeaseDetails : {}", id);
        return leaseDetailsRepository.findById(id)
            .map(leaseDetailsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaseDetails : {}", id);
        leaseDetailsRepository.deleteById(id);
        leaseDetailsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaseDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeaseDetails for query {}", query);
        return leaseDetailsSearchRepository.search(queryStringQuery(query), pageable)
            .map(leaseDetailsMapper::toDto);
    }
}
