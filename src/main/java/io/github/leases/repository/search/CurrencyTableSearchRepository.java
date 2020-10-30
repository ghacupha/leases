package io.github.leases.repository.search;

import io.github.leases.domain.CurrencyTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link CurrencyTable} entity.
 */
public interface CurrencyTableSearchRepository extends ElasticsearchRepository<CurrencyTable, Long> {
}
