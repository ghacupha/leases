package io.github.leases.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class ContractualLeaseRentalTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractualLeaseRental.class);
        ContractualLeaseRental contractualLeaseRental1 = new ContractualLeaseRental();
        contractualLeaseRental1.setId(1L);
        ContractualLeaseRental contractualLeaseRental2 = new ContractualLeaseRental();
        contractualLeaseRental2.setId(contractualLeaseRental1.getId());
        assertThat(contractualLeaseRental1).isEqualTo(contractualLeaseRental2);
        contractualLeaseRental2.setId(2L);
        assertThat(contractualLeaseRental1).isNotEqualTo(contractualLeaseRental2);
        contractualLeaseRental1.setId(null);
        assertThat(contractualLeaseRental1).isNotEqualTo(contractualLeaseRental2);
    }
}
