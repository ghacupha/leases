package io.github.leases.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link io.github.leases.domain.LeaseDetails} entity. This class is used
 * in {@link io.github.leases.web.rest.LeaseDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lease-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaseDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leaseContractNumber;

    private DoubleFilter incrementalBorrowingRate;

    private LocalDateFilter commencementDate;

    private BigDecimalFilter leasePrepayments;

    private BigDecimalFilter initialDirectCosts;

    private BigDecimalFilter demolitionCosts;

    private StringFilter assetAccountNumber;

    private StringFilter liabilityAccountNumber;

    private StringFilter depreciationAccountNumber;

    private StringFilter interestAccountNumber;

    public LeaseDetailsCriteria() {
    }

    public LeaseDetailsCriteria(LeaseDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leaseContractNumber = other.leaseContractNumber == null ? null : other.leaseContractNumber.copy();
        this.incrementalBorrowingRate = other.incrementalBorrowingRate == null ? null : other.incrementalBorrowingRate.copy();
        this.commencementDate = other.commencementDate == null ? null : other.commencementDate.copy();
        this.leasePrepayments = other.leasePrepayments == null ? null : other.leasePrepayments.copy();
        this.initialDirectCosts = other.initialDirectCosts == null ? null : other.initialDirectCosts.copy();
        this.demolitionCosts = other.demolitionCosts == null ? null : other.demolitionCosts.copy();
        this.assetAccountNumber = other.assetAccountNumber == null ? null : other.assetAccountNumber.copy();
        this.liabilityAccountNumber = other.liabilityAccountNumber == null ? null : other.liabilityAccountNumber.copy();
        this.depreciationAccountNumber = other.depreciationAccountNumber == null ? null : other.depreciationAccountNumber.copy();
        this.interestAccountNumber = other.interestAccountNumber == null ? null : other.interestAccountNumber.copy();
    }

    @Override
    public LeaseDetailsCriteria copy() {
        return new LeaseDetailsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public void setLeaseContractNumber(StringFilter leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public DoubleFilter getIncrementalBorrowingRate() {
        return incrementalBorrowingRate;
    }

    public void setIncrementalBorrowingRate(DoubleFilter incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
    }

    public LocalDateFilter getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDateFilter commencementDate) {
        this.commencementDate = commencementDate;
    }

    public BigDecimalFilter getLeasePrepayments() {
        return leasePrepayments;
    }

    public void setLeasePrepayments(BigDecimalFilter leasePrepayments) {
        this.leasePrepayments = leasePrepayments;
    }

    public BigDecimalFilter getInitialDirectCosts() {
        return initialDirectCosts;
    }

    public void setInitialDirectCosts(BigDecimalFilter initialDirectCosts) {
        this.initialDirectCosts = initialDirectCosts;
    }

    public BigDecimalFilter getDemolitionCosts() {
        return demolitionCosts;
    }

    public void setDemolitionCosts(BigDecimalFilter demolitionCosts) {
        this.demolitionCosts = demolitionCosts;
    }

    public StringFilter getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public void setAssetAccountNumber(StringFilter assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
    }

    public StringFilter getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public void setLiabilityAccountNumber(StringFilter liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
    }

    public StringFilter getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public void setDepreciationAccountNumber(StringFilter depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
    }

    public StringFilter getInterestAccountNumber() {
        return interestAccountNumber;
    }

    public void setInterestAccountNumber(StringFilter interestAccountNumber) {
        this.interestAccountNumber = interestAccountNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeaseDetailsCriteria that = (LeaseDetailsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(leaseContractNumber, that.leaseContractNumber) &&
            Objects.equals(incrementalBorrowingRate, that.incrementalBorrowingRate) &&
            Objects.equals(commencementDate, that.commencementDate) &&
            Objects.equals(leasePrepayments, that.leasePrepayments) &&
            Objects.equals(initialDirectCosts, that.initialDirectCosts) &&
            Objects.equals(demolitionCosts, that.demolitionCosts) &&
            Objects.equals(assetAccountNumber, that.assetAccountNumber) &&
            Objects.equals(liabilityAccountNumber, that.liabilityAccountNumber) &&
            Objects.equals(depreciationAccountNumber, that.depreciationAccountNumber) &&
            Objects.equals(interestAccountNumber, that.interestAccountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        leaseContractNumber,
        incrementalBorrowingRate,
        commencementDate,
        leasePrepayments,
        initialDirectCosts,
        demolitionCosts,
        assetAccountNumber,
        liabilityAccountNumber,
        depreciationAccountNumber,
        interestAccountNumber
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaseDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (leaseContractNumber != null ? "leaseContractNumber=" + leaseContractNumber + ", " : "") +
                (incrementalBorrowingRate != null ? "incrementalBorrowingRate=" + incrementalBorrowingRate + ", " : "") +
                (commencementDate != null ? "commencementDate=" + commencementDate + ", " : "") +
                (leasePrepayments != null ? "leasePrepayments=" + leasePrepayments + ", " : "") +
                (initialDirectCosts != null ? "initialDirectCosts=" + initialDirectCosts + ", " : "") +
                (demolitionCosts != null ? "demolitionCosts=" + demolitionCosts + ", " : "") +
                (assetAccountNumber != null ? "assetAccountNumber=" + assetAccountNumber + ", " : "") +
                (liabilityAccountNumber != null ? "liabilityAccountNumber=" + liabilityAccountNumber + ", " : "") +
                (depreciationAccountNumber != null ? "depreciationAccountNumber=" + depreciationAccountNumber + ", " : "") +
                (interestAccountNumber != null ? "interestAccountNumber=" + interestAccountNumber + ", " : "") +
            "}";
    }

}
