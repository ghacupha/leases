package io.github.leases.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeasesFileTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasesFileType.class);
        LeasesFileType leasesFileType1 = new LeasesFileType();
        leasesFileType1.setId(1L);
        LeasesFileType leasesFileType2 = new LeasesFileType();
        leasesFileType2.setId(leasesFileType1.getId());
        assertThat(leasesFileType1).isEqualTo(leasesFileType2);
        leasesFileType2.setId(2L);
        assertThat(leasesFileType1).isNotEqualTo(leasesFileType2);
        leasesFileType1.setId(null);
        assertThat(leasesFileType1).isNotEqualTo(leasesFileType2);
    }
}
