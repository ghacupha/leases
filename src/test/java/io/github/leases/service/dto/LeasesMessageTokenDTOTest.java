package io.github.leases.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeasesMessageTokenDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasesMessageTokenDTO.class);
        LeasesMessageTokenDTO leasesMessageTokenDTO1 = new LeasesMessageTokenDTO();
        leasesMessageTokenDTO1.setId(1L);
        LeasesMessageTokenDTO leasesMessageTokenDTO2 = new LeasesMessageTokenDTO();
        assertThat(leasesMessageTokenDTO1).isNotEqualTo(leasesMessageTokenDTO2);
        leasesMessageTokenDTO2.setId(leasesMessageTokenDTO1.getId());
        assertThat(leasesMessageTokenDTO1).isEqualTo(leasesMessageTokenDTO2);
        leasesMessageTokenDTO2.setId(2L);
        assertThat(leasesMessageTokenDTO1).isNotEqualTo(leasesMessageTokenDTO2);
        leasesMessageTokenDTO1.setId(null);
        assertThat(leasesMessageTokenDTO1).isNotEqualTo(leasesMessageTokenDTO2);
    }
}
