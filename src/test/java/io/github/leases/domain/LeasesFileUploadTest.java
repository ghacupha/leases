package io.github.leases.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeasesFileUploadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasesFileUpload.class);
        LeasesFileUpload leasesFileUpload1 = new LeasesFileUpload();
        leasesFileUpload1.setId(1L);
        LeasesFileUpload leasesFileUpload2 = new LeasesFileUpload();
        leasesFileUpload2.setId(leasesFileUpload1.getId());
        assertThat(leasesFileUpload1).isEqualTo(leasesFileUpload2);
        leasesFileUpload2.setId(2L);
        assertThat(leasesFileUpload1).isNotEqualTo(leasesFileUpload2);
        leasesFileUpload1.setId(null);
        assertThat(leasesFileUpload1).isNotEqualTo(leasesFileUpload2);
    }
}
