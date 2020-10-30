package io.github.leases.service.impl;

import io.github.leases.service.LeasesFileUploadService;
import io.github.leases.domain.LeasesFileUpload;
import io.github.leases.repository.LeasesFileUploadRepository;
import io.github.leases.repository.search.LeasesFileUploadSearchRepository;
import io.github.leases.service.dto.LeasesFileUploadDTO;
import io.github.leases.service.mapper.LeasesFileUploadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link LeasesFileUpload}.
 */
@Service
@Transactional
public class LeasesFileUploadServiceImpl implements LeasesFileUploadService {

    private final Logger log = LoggerFactory.getLogger(LeasesFileUploadServiceImpl.class);

    private final LeasesFileUploadRepository leasesFileUploadRepository;

    private final LeasesFileUploadMapper leasesFileUploadMapper;

    private final LeasesFileUploadSearchRepository leasesFileUploadSearchRepository;

    public LeasesFileUploadServiceImpl(LeasesFileUploadRepository leasesFileUploadRepository, LeasesFileUploadMapper leasesFileUploadMapper, LeasesFileUploadSearchRepository leasesFileUploadSearchRepository) {
        this.leasesFileUploadRepository = leasesFileUploadRepository;
        this.leasesFileUploadMapper = leasesFileUploadMapper;
        this.leasesFileUploadSearchRepository = leasesFileUploadSearchRepository;
    }

    @Override
    public LeasesFileUploadDTO save(LeasesFileUploadDTO leasesFileUploadDTO) {
        log.debug("Request to save LeasesFileUpload : {}", leasesFileUploadDTO);
        LeasesFileUpload leasesFileUpload = leasesFileUploadMapper.toEntity(leasesFileUploadDTO);
        leasesFileUpload = leasesFileUploadRepository.save(leasesFileUpload);
        LeasesFileUploadDTO result = leasesFileUploadMapper.toDto(leasesFileUpload);
        leasesFileUploadSearchRepository.save(leasesFileUpload);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasesFileUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeasesFileUploads");
        return leasesFileUploadRepository.findAll(pageable)
            .map(leasesFileUploadMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<LeasesFileUploadDTO> findOne(Long id) {
        log.debug("Request to get LeasesFileUpload : {}", id);
        return leasesFileUploadRepository.findById(id)
            .map(leasesFileUploadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeasesFileUpload : {}", id);
        leasesFileUploadRepository.deleteById(id);
        leasesFileUploadSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeasesFileUploadDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LeasesFileUploads for query {}", query);
        return leasesFileUploadSearchRepository.search(queryStringQuery(query), pageable)
            .map(leasesFileUploadMapper::toDto);
    }
}
