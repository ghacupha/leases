package io.github.leases.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LeaseDetailsSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LeaseDetailsSearchRepositoryMockConfiguration {

    @MockBean
    private LeaseDetailsSearchRepository mockLeaseDetailsSearchRepository;

}
