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
 * Criteria class for the {@link io.github.leases.domain.ContractualLeaseRental} entity. This class is used
 * in {@link io.github.leases.web.rest.ContractualLeaseRentalResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contractual-lease-rentals?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContractualLeaseRentalCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter leaseContractNumber;

    private IntegerFilter rentalSequenceNumber;

    private LocalDateFilter leaseRentalDate;

    private BigDecimalFilter leaseRentalAmount;

    public ContractualLeaseRentalCriteria() {
    }

    public ContractualLeaseRentalCriteria(ContractualLeaseRentalCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.leaseContractNumber = other.leaseContractNumber == null ? null : other.leaseContractNumber.copy();
        this.rentalSequenceNumber = other.rentalSequenceNumber == null ? null : other.rentalSequenceNumber.copy();
        this.leaseRentalDate = other.leaseRentalDate == null ? null : other.leaseRentalDate.copy();
        this.leaseRentalAmount = other.leaseRentalAmount == null ? null : other.leaseRentalAmount.copy();
    }

    @Override
    public ContractualLeaseRentalCriteria copy() {
        return new ContractualLeaseRentalCriteria(this);
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

    public IntegerFilter getRentalSequenceNumber() {
        return rentalSequenceNumber;
    }

    public void setRentalSequenceNumber(IntegerFilter rentalSequenceNumber) {
        this.rentalSequenceNumber = rentalSequenceNumber;
    }

    public LocalDateFilter getLeaseRentalDate() {
        return leaseRentalDate;
    }

    public void setLeaseRentalDate(LocalDateFilter leaseRentalDate) {
        this.leaseRentalDate = leaseRentalDate;
    }

    public BigDecimalFilter getLeaseRentalAmount() {
        return leaseRentalAmount;
    }

    public void setLeaseRentalAmount(BigDecimalFilter leaseRentalAmount) {
        this.leaseRentalAmount = leaseRentalAmount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContractualLeaseRentalCriteria that = (ContractualLeaseRentalCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(leaseContractNumber, that.leaseContractNumber) &&
            Objects.equals(rentalSequenceNumber, that.rentalSequenceNumber) &&
            Objects.equals(leaseRentalDate, that.leaseRentalDate) &&
            Objects.equals(leaseRentalAmount, that.leaseRentalAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        leaseContractNumber,
        rentalSequenceNumber,
        leaseRentalDate,
        leaseRentalAmount
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractualLeaseRentalCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (leaseContractNumber != null ? "leaseContractNumber=" + leaseContractNumber + ", " : "") +
                (rentalSequenceNumber != null ? "rentalSequenceNumber=" + rentalSequenceNumber + ", " : "") +
                (leaseRentalDate != null ? "leaseRentalDate=" + leaseRentalDate + ", " : "") +
                (leaseRentalAmount != null ? "leaseRentalAmount=" + leaseRentalAmount + ", " : "") +
            "}";
    }

}
