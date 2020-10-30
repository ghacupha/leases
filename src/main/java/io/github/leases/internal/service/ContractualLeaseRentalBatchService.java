package io.github.leases.internal.service;

import io.github.leases.repository.ContractualLeaseRentalRepository;
import io.github.leases.repository.search.ContractualLeaseRentalSearchRepository;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import io.github.leases.service.mapper.ContractualLeaseRentalMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("contractualLeaseRentalBatchService")
public class ContractualLeaseRentalBatchService implements BatchService<ContractualLeaseRentalDTO> {

    private final ContractualLeaseRentalMapper mapper;
    private final ContractualLeaseRentalRepository repository;
    private final ContractualLeaseRentalSearchRepository searchRepository;

    public ContractualLeaseRentalBatchService(ContractualLeaseRentalMapper mapper, ContractualLeaseRentalRepository repository, ContractualLeaseRentalSearchRepository searchRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.searchRepository = searchRepository;
    }

    @Override
    public List<ContractualLeaseRentalDTO> save(List<ContractualLeaseRentalDTO> entities) {
        return mapper.toDto(repository.saveAll(mapper.toEntity(entities)));
    }

    @Override
    public void index(List<ContractualLeaseRentalDTO> entities) {

        searchRepository.saveAll(mapper.toEntity(entities));
    }
}
