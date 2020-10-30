package io.github.leases.repository.search;

import io.github.leases.domain.ContractualLeaseRental;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ContractualLeaseRental} entity.
 */
public interface ContractualLeaseRentalSearchRepository extends ElasticsearchRepository<ContractualLeaseRental, Long> {
}
