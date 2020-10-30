package io.github.leases.repository.search;

import io.github.leases.domain.LeasesMessageToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link LeasesMessageToken} entity.
 */
public interface LeasesMessageTokenSearchRepository extends ElasticsearchRepository<LeasesMessageToken, Long> {
}
