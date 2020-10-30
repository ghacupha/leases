package io.github.leases.service.mapper;


import io.github.leases.domain.*;
import io.github.leases.service.dto.LeasesFileUploadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeasesFileUpload} and its DTO {@link LeasesFileUploadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeasesFileUploadMapper extends EntityMapper<LeasesFileUploadDTO, LeasesFileUpload> {



    default LeasesFileUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeasesFileUpload leasesFileUpload = new LeasesFileUpload();
        leasesFileUpload.setId(id);
        return leasesFileUpload;
    }
}
