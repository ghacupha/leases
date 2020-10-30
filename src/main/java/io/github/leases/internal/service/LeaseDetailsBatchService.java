package io.github.leases.internal.service;

import io.github.leases.repository.ContractualLeaseRentalRepository;
import io.github.leases.repository.LeaseDetailsRepository;
import io.github.leases.repository.search.LeaseDetailsSearchRepository;
import io.github.leases.service.dto.LeaseDetailsDTO;
import io.github.leases.service.mapper.LeaseDetailsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("leaseDetailsBatchService")
public class LeaseDetailsBatchService implements BatchService<LeaseDetailsDTO> {

    private final LeaseDetailsMapper mapper;
    private final LeaseDetailsRepository repository;
    private final LeaseDetailsSearchRepository searchRepository;

    public LeaseDetailsBatchService(LeaseDetailsMapper mapper, LeaseDetailsRepository repository, LeaseDetailsSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<LeaseDetailsDTO> save(List<LeaseDetailsDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<LeaseDetailsDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
