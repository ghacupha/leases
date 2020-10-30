package io.github.leases.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LeasesFileUploadMapperTest {

    private LeasesFileUploadMapper leasesFileUploadMapper;

    @BeforeEach
    public void setUp() {
        leasesFileUploadMapper = new LeasesFileUploadMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leasesFileUploadMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leasesFileUploadMapper.fromId(null)).isNull();
    }
}
