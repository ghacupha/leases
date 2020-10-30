package io.github.leases.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeaseDetailsDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseDetailsDTO.class);
        LeaseDetailsDTO leaseDetailsDTO1 = new LeaseDetailsDTO();
        leaseDetailsDTO1.setId(1L);
        LeaseDetailsDTO leaseDetailsDTO2 = new LeaseDetailsDTO();
        assertThat(leaseDetailsDTO1).isNotEqualTo(leaseDetailsDTO2);
        leaseDetailsDTO2.setId(leaseDetailsDTO1.getId());
        assertThat(leaseDetailsDTO1).isEqualTo(leaseDetailsDTO2);
        leaseDetailsDTO2.setId(2L);
        assertThat(leaseDetailsDTO1).isNotEqualTo(leaseDetailsDTO2);
        leaseDetailsDTO1.setId(null);
        assertThat(leaseDetailsDTO1).isNotEqualTo(leaseDetailsDTO2);
    }
}
