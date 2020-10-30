package io.github.leases.repository.search;

import io.github.leases.domain.LeasesFileUpload;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link LeasesFileUpload} entity.
 */
public interface LeasesFileUploadSearchRepository extends ElasticsearchRepository<LeasesFileUpload, Long> {
}
