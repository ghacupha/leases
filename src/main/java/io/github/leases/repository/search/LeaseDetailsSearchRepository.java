package io.github.leases.repository.search;

import io.github.leases.domain.LeaseDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link LeaseDetails} entity.
 */
public interface LeaseDetailsSearchRepository extends ElasticsearchRepository<LeaseDetails, Long> {
}
