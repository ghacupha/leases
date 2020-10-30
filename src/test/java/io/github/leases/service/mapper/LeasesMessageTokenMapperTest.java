package io.github.leases.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LeasesMessageTokenMapperTest {

    private LeasesMessageTokenMapper leasesMessageTokenMapper;

    @BeforeEach
    public void setUp() {
        leasesMessageTokenMapper = new LeasesMessageTokenMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leasesMessageTokenMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leasesMessageTokenMapper.fromId(null)).isNull();
    }
}
