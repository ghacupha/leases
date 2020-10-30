package io.github.leases.service.impl;

import io.github.leases.service.LeasesMessageTokenService;
import io.github.leases.domain.LeasesMessageToken;
import io.github.leases.repository.LeasesMessageTokenRepository;
import io.github.leases.repository.search.LeasesMessageTokenSearchRepository;
import io.github.leases.service.dto.LeasesMessageTokenDTO;
import io.github.leases.service.mapper.LeasesMessageTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link LeasesMessageToken}.
 */
@Service
@Transactional
public class LeasesMessageTokenServiceImpl implements LeasesMessageTokenService {

    private final Logger log = LoggerFactory.getLogger(LeasesMessageTokenServiceImpl.class);

    private final LeasesMessageTokenRepository leasesMessageTokenRepository;

    private final LeasesMessageTokenMapper leasesMessageTokenMapper;

    private final LeasesMessageTokenSearchRepository leasesMessageTokenSearchRepository;

    public LeasesMessageTokenServiceImpl(LeasesMessageTokenRepository leasesMessageTokenRepository, LeasesMessageTokenMapper leasesMessageTokenMapper, LeasesMessageTokenSearchRepository leasesMessageTokenSearchRepository) {
        this.leasesMessageTokenRepository = leasesMessageTokenRepository;
        this.leasesMessageTokenMapper = leasesMessageTokenMapper;
        this.leasesMessageTokenSearchRepository = leasesMessageTokenSearchRepository;
    }

    @Override
    public LeasesMessageTokenDTO save(LeasesMessageTokenDTO leasesMessageTokenDTO) {
        log.debug("Request to save LeasesMessageToken : {}", leasesMessageTokenDTO);
        LeasesMessageToken leasesMessageToken = leasesMessageTokenMapper.toEntity(leasesMessageTokenDTO);
        leasesMessageToken = leasesMessageTokenRepository.save(leasesMessageToken);
        LeasesMessageTokenDTO result = leasesMessageTokenMapper.toDto(leasesMessageToken);
        leasesMessageTokenSearchRepository.save(leasesMessageToken);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasesMessageTokenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeasesMessageTokens");
        return leasesMessageTokenRepository.findAll(pageable)
            .map(leasesMessageTokenMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<LeasesMessageTokenDTO> findOne(Long id) {
        log.debug("Request to get LeasesMessageToken : {}", id);
        return leasesMessageTokenRepository.findById(id)
            .map(leasesMessageTokenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeasesMessageToken : {}", id);
        leasesMessageTokenRepository.deleteById(id);
        leasesMessageTokenSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasesMessageTokenDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasesMessageTokens for query {}", query);
        return leasesMessageTokenSearchRepository.search(queryStringQuery(query), pageable)
            .map(leasesMessageTokenMapper::toDto);
    }
}
