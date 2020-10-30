package io.github.leases.service.mapper;


import io.github.leases.domain.*;
import io.github.leases.service.dto.LeasesMessageTokenDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeasesMessageToken} and its DTO {@link LeasesMessageTokenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeasesMessageTokenMapper extends EntityMapper<LeasesMessageTokenDTO, LeasesMessageToken> {



    default LeasesMessageToken fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeasesMessageToken leasesMessageToken = new LeasesMessageToken();
        leasesMessageToken.setId(id);
        return leasesMessageToken;
    }
}
