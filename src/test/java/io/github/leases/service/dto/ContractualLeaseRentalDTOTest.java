package io.github.leases.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class ContractualLeaseRentalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractualLeaseRentalDTO.class);
        ContractualLeaseRentalDTO contractualLeaseRentalDTO1 = new ContractualLeaseRentalDTO();
        contractualLeaseRentalDTO1.setId(1L);
        ContractualLeaseRentalDTO contractualLeaseRentalDTO2 = new ContractualLeaseRentalDTO();
        assertThat(contractualLeaseRentalDTO1).isNotEqualTo(contractualLeaseRentalDTO2);
        contractualLeaseRentalDTO2.setId(contractualLeaseRentalDTO1.getId());
        assertThat(contractualLeaseRentalDTO1).isEqualTo(contractualLeaseRentalDTO2);
        contractualLeaseRentalDTO2.setId(2L);
        assertThat(contractualLeaseRentalDTO1).isNotEqualTo(contractualLeaseRentalDTO2);
        contractualLeaseRentalDTO1.setId(null);
        assertThat(contractualLeaseRentalDTO1).isNotEqualTo(contractualLeaseRentalDTO2);
    }
}
