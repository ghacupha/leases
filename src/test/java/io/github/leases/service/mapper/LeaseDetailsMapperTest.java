package io.github.leases.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LeaseDetailsMapperTest {

    private LeaseDetailsMapper leaseDetailsMapper;

    @BeforeEach
    public void setUp() {
        leaseDetailsMapper = new LeaseDetailsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaseDetailsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaseDetailsMapper.fromId(null)).isNull();
    }
}
