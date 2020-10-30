package io.github.leases.internal.model;

import io.github.leases.internal.Mapping;
import io.github.leases.service.dto.LeaseDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.leases.internal.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;

@Mapper(componentModel = "spring", uses = {})
public interface LeaseDetailsEVMDTOMapping extends Mapping<LeaseDetailsEVM, LeaseDetailsDTO> {

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leasePrepayments", source = "leasePrepayments"),
            @org.mapstruct.Mapping(target = "initialDirectCosts", source = "initialDirectCosts"),
            @org.mapstruct.Mapping(target = "demolitionCosts", source = "demolitionCosts"),
        }
    )
    default BigDecimal toBigDecimalMap(Double doublePrecisionAmount) {
        return toScaledBigDecimal(doublePrecisionAmount);
    }

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "leasePrepayments", source = "leasePrepayments"),
            @org.mapstruct.Mapping(target = "initialDirectCosts", source = "initialDirectCosts"),
            @org.mapstruct.Mapping(target = "demolitionCosts", source = "demolitionCosts"),
        }
    )
    default Double toDoubleMap(BigDecimal toDoublePrecisionAmount) {
        return toDouble(toDoublePrecisionAmount);
    }

    @Mappings(
        value = {
            @org.mapstruct.Mapping(target = "commencementDate", source = "commencementDate"),
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
            @org.mapstruct.Mapping(target = "commencementDate", source = "commencementDate"),
        }
    )
    default String toStringDateMap(LocalDate toStringDate) {
        if (toStringDate == null) {
            return "";
        }
        return DATETIME_FORMATTER.format(toStringDate);
    }
}
