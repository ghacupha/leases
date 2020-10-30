package io.github.leases.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link io.github.leases.domain.ContractualLeaseRental} entity.
 */
public class ContractualLeaseRentalDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String leaseContractNumber;

    @NotNull
    private Integer rentalSequenceNumber;

    private LocalDate leaseRentalDate;

    private BigDecimal leaseRentalAmount;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public Integer getRentalSequenceNumber() {
        return rentalSequenceNumber;
    }

    public void setRentalSequenceNumber(Integer rentalSequenceNumber) {
        this.rentalSequenceNumber = rentalSequenceNumber;
    }

    public LocalDate getLeaseRentalDate() {
        return leaseRentalDate;
    }

    public void setLeaseRentalDate(LocalDate leaseRentalDate) {
        this.leaseRentalDate = leaseRentalDate;
    }

    public BigDecimal getLeaseRentalAmount() {
        return leaseRentalAmount;
    }

    public void setLeaseRentalAmount(BigDecimal leaseRentalAmount) {
        this.leaseRentalAmount = leaseRentalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractualLeaseRentalDTO)) {
            return false;
        }

        return id != null && id.equals(((ContractualLeaseRentalDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractualLeaseRentalDTO{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", rentalSequenceNumber=" + getRentalSequenceNumber() +
            ", leaseRentalDate='" + getLeaseRentalDate() + "'" +
            ", leaseRentalAmount=" + getLeaseRentalAmount() +
            "}";
    }
}
