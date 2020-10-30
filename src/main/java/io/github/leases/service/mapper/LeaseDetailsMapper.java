package io.github.leases.service.mapper;


import io.github.leases.domain.*;
import io.github.leases.service.dto.LeaseDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaseDetails} and its DTO {@link LeaseDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaseDetailsMapper extends EntityMapper<LeaseDetailsDTO, LeaseDetails> {



    default LeaseDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaseDetails leaseDetails = new LeaseDetails();
        leaseDetails.setId(id);
        return leaseDetails;
    }
}
