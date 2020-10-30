package io.github.leases.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A LeaseDetails.
 */
@Entity
@Table(name = "lease_details")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "leasedetails")
public class LeaseDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "lease_contract_number", nullable = false)
    private String leaseContractNumber;

    @Column(name = "incremental_borrowing_rate")
    private Double incrementalBorrowingRate;

    @Column(name = "commencement_date")
    private LocalDate commencementDate;

    @Column(name = "lease_prepayments", precision = 21, scale = 2)
    private BigDecimal leasePrepayments;

    @Column(name = "initial_direct_costs", precision = 21, scale = 2)
    private BigDecimal initialDirectCosts;

    @Column(name = "demolition_costs", precision = 21, scale = 2)
    private BigDecimal demolitionCosts;

    @Column(name = "asset_account_number")
    private String assetAccountNumber;

    @Column(name = "liability_account_number")
    private String liabilityAccountNumber;

    @Column(name = "depreciation_account_number")
    private String depreciationAccountNumber;

    @Column(name = "interest_account_number")
    private String interestAccountNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public LeaseDetails leaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public Double getIncrementalBorrowingRate() {
        return incrementalBorrowingRate;
    }

    public LeaseDetails incrementalBorrowingRate(Double incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
        return this;
    }

    public void setIncrementalBorrowingRate(Double incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public LeaseDetails commencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
        return this;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public BigDecimal getLeasePrepayments() {
        return leasePrepayments;
    }

    public LeaseDetails leasePrepayments(BigDecimal leasePrepayments) {
        this.leasePrepayments = leasePrepayments;
        return this;
    }

    public void setLeasePrepayments(BigDecimal leasePrepayments) {
        this.leasePrepayments = leasePrepayments;
    }

    public BigDecimal getInitialDirectCosts() {
        return initialDirectCosts;
    }

    public LeaseDetails initialDirectCosts(BigDecimal initialDirectCosts) {
        this.initialDirectCosts = initialDirectCosts;
        return this;
    }

    public void setInitialDirectCosts(BigDecimal initialDirectCosts) {
        this.initialDirectCosts = initialDirectCosts;
    }

    public BigDecimal getDemolitionCosts() {
        return demolitionCosts;
    }

    public LeaseDetails demolitionCosts(BigDecimal demolitionCosts) {
        this.demolitionCosts = demolitionCosts;
        return this;
    }

    public void setDemolitionCosts(BigDecimal demolitionCosts) {
        this.demolitionCosts = demolitionCosts;
    }

    public String getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public LeaseDetails assetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
        return this;
    }

    public void setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public String getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public LeaseDetails liabilityAccountNumber(String liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
        return this;
    }

    public void setLiabilityAccountNumber(String liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
    }

    public String getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public LeaseDetails depreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
        return this;
    }

    public void setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public String getInterestAccountNumber() {
        return interestAccountNumber;
    }

    public LeaseDetails interestAccountNumber(String interestAccountNumber) {
        this.interestAccountNumber = interestAccountNumber;
        return this;
    }

    public void setInterestAccountNumber(String interestAccountNumber) {
        this.interestAccountNumber = interestAccountNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaseDetails)) {
            return false;
        }
        return id != null && id.equals(((LeaseDetails) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseDetails{" +
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
