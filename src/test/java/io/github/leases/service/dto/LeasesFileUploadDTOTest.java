package io.github.leases.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.github.leases.web.rest.TestUtil;

public class LeasesFileUploadDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeasesFileUploadDTO.class);
        LeasesFileUploadDTO leasesFileUploadDTO1 = new LeasesFileUploadDTO();
        leasesFileUploadDTO1.setId(1L);
        LeasesFileUploadDTO leasesFileUploadDTO2 = new LeasesFileUploadDTO();
        assertThat(leasesFileUploadDTO1).isNotEqualTo(leasesFileUploadDTO2);
        leasesFileUploadDTO2.setId(leasesFileUploadDTO1.getId());
        assertThat(leasesFileUploadDTO1).isEqualTo(leasesFileUploadDTO2);
        leasesFileUploadDTO2.setId(2L);
        assertThat(leasesFileUploadDTO1).isNotEqualTo(leasesFileUploadDTO2);
        leasesFileUploadDTO1.setId(null);
        assertThat(leasesFileUploadDTO1).isNotEqualTo(leasesFileUploadDTO2);
    }
}
