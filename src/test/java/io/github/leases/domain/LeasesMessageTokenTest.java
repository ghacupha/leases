package io.github.leases.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeasesMessageTokenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasesMessageToken.class);
        LeasesMessageToken leasesMessageToken1 = new LeasesMessageToken();
        leasesMessageToken1.setId(1L);
        LeasesMessageToken leasesMessageToken2 = new LeasesMessageToken();
        leasesMessageToken2.setId(leasesMessageToken1.getId());
        assertThat(leasesMessageToken1).isEqualTo(leasesMessageToken2);
        leasesMessageToken2.setId(2L);
        assertThat(leasesMessageToken1).isNotEqualTo(leasesMessageToken2);
        leasesMessageToken1.setId(null);
        assertThat(leasesMessageToken1).isNotEqualTo(leasesMessageToken2);
    }
}
