package io.github.leases.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link io.github.leases.domain.LeaseDetails} entity.
 */
public class LeaseDetailsDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String leaseContractNumber;

    private Double incrementalBorrowingRate;

    private LocalDate commencementDate;

    private BigDecimal leasePrepayments;

    private BigDecimal initialDirectCosts;

    private BigDecimal demolitionCosts;

    private String assetAccountNumber;

    private String liabilityAccountNumber;

    private String depreciationAccountNumber;

    private String interestAccountNumber;

    
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

    public Double getIncrementalBorrowingRate() {
        return incrementalBorrowingRate;
    }

    public void setIncrementalBorrowingRate(Double incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public BigDecimal getLeasePrepayments() {
        return leasePrepayments;
    }

    public void setLeasePrepayments(BigDecimal leasePrepayments) {
        this.leasePrepayments = leasePrepayments;
    }

    public BigDecimal getInitialDirectCosts() {
        return initialDirectCosts;
    }

    public void setInitialDirectCosts(BigDecimal initialDirectCosts) {
        this.initialDirectCosts = initialDirectCosts;
    }

    public BigDecimal getDemolitionCosts() {
        return demolitionCosts;
    }

    public void setDemolitionCosts(BigDecimal demolitionCosts) {
        this.demolitionCosts = demolitionCosts;
    }

    public String getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public void setLiabilityAccountNumber(String liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public String getInterestAccountNumber() {
        return interestAccountNumber;
    }

    public void setInterestAccountNumber(String interestAccountNumber) {
        this.interestAccountNumber = interestAccountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((LeaseDetailsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseDetailsDTO{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", incrementalBorrowingRate=" + getIncrementalBorrowingRate() +
            ", commencementDate='" + getCommencementDate() + "'" +
            ", leasePrepayments=" + getLeasePrepayments() +
            ", initialDirectCosts=" + getInitialDirectCosts() +
            ", demolitionCosts=" + getDemolitionCosts() +
            ", assetAccountNumber='" + getAssetAccountNumber() + "'" +
            ", liabilityAccountNumber='" + getLiabilityAccountNumber() + "'" +
            ", depreciationAccountNumber='" + getDepreciationAccountNumber() + "'" +
            ", interestAccountNumber='" + getInterestAccountNumber() + "'" +
            "}";
    }
}
