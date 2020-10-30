package io.github.leases.service.mapper;


import io.github.leases.domain.*;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractualLeaseRental} and its DTO {@link ContractualLeaseRentalDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContractualLeaseRentalMapper extends EntityMapper<ContractualLeaseRentalDTO, ContractualLeaseRental> {



    default ContractualLeaseRental fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContractualLeaseRental contractualLeaseRental = new ContractualLeaseRental();
        contractualLeaseRental.setId(id);
        return contractualLeaseRental;
    }
}
