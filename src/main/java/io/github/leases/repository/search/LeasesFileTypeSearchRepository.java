package io.github.leases.repository.search;

import io.github.leases.domain.LeasesFileType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link LeasesFileType} entity.
 */
public interface LeasesFileTypeSearchRepository extends ElasticsearchRepository<LeasesFileType, Long> {
}
