package io.github.leases.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeaseDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseDetails.class);
        LeaseDetails leaseDetails1 = new LeaseDetails();
        leaseDetails1.setId(1L);
        LeaseDetails leaseDetails2 = new LeaseDetails();
        leaseDetails2.setId(leaseDetails1.getId());
        assertThat(leaseDetails1).isEqualTo(leaseDetails2);
        leaseDetails2.setId(2L);
        assertThat(leaseDetails1).isNotEqualTo(leaseDetails2);
        leaseDetails1.setId(null);
        assertThat(leaseDetails1).isNotEqualTo(leaseDetails2);
    }
}
