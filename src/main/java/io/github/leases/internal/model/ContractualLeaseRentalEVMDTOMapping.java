package io.github.leases.internal.model;

import io.github.leases.internal.Mapping;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.leases.internal.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;

@Mapper(componentModel = "spring", uses = {})
public interface ContractualLeaseRentalEVMDTOMapping extends Mapping<ContractualLeaseRentalEVM, ContractualLeaseRentalDTO> {

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leaseRentalAmount", source = "leaseRentalAmount"),
        }
    )
    default BigDecimal toBigDecimalMap(Double doublePrecisionAmount) {
        return toScaledBigDecimal(doublePrecisionAmount);
    }

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leaseRentalAmount", source = "leaseRentalAmount"),
        }
    )
    default Double toDoubleMap(BigDecimal toDoublePrecisionAmount) {
        return toDouble(toDoublePrecisionAmount);
    }

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leaseRentalDate", source = "leaseRentalDate"),
        }
    )
    default LocalDate toLocalDateMap(String stringDate) {
        if (stringDate == null) {
            return null;
        }
        return LocalDate.parse(stringDate, DATETIME_FORMATTER);
    }

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leaseRentalDate", source = "leaseRentalDate"),
        }
    )
    default String toStringDateMap(LocalDate toStringDate) {
        if (toStringDate == null) {
            return "";
        }
        return DATETIME_FORMATTER.format(toStringDate);
    }
}
