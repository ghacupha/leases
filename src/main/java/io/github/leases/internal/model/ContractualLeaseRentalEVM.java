package io.github.leases.internal.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

import java.io.Serializable;

/**
 * Excel view model for contractual-lease-rental entity
 */
public class ContractualLeaseRentalEVM implements Serializable {

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String leaseContractNumber;

    @ExcelCell(1)
    private String rentalSequenceNumber;

    @ExcelCell(2)
    private String leaseRentalDate;

    @ExcelCell(3)
    private double leaseRentalAmount;

    public ContractualLeaseRentalEVM(int rowIndex, String leaseContractNumber, String rentalSequenceNumber, String leaseRentalDate, double leaseRentalAmount) {
        this.rowIndex = rowIndex;
        this.leaseContractNumber = leaseContractNumber;
        this.rentalSequenceNumber = rentalSequenceNumber;
        this.leaseRentalDate = leaseRentalDate;
        this.leaseRentalAmount = leaseRentalAmount;
    }

    public ContractualLeaseRentalEVM() {
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public ContractualLeaseRentalEVM setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public ContractualLeaseRentalEVM setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
        return this;
    }

    public String getRentalSequenceNumber() {
        return rentalSequenceNumber;
    }

    public ContractualLeaseRentalEVM setRentalSequenceNumber(String rentalSequenceNumber) {
        this.rentalSequenceNumber = rentalSequenceNumber;
        return this;
    }

    public String getLeaseRentalDate() {
        return leaseRentalDate;
    }

    public ContractualLeaseRentalEVM setLeaseRentalDate(String leaseRentalDate) {
        this.leaseRentalDate = leaseRentalDate;
        return this;
    }

    public double getLeaseRentalAmount() {
        return leaseRentalAmount;
    }

    public ContractualLeaseRentalEVM setLeaseRentalAmount(double leaseRentalAmount) {
        this.leaseRentalAmount = leaseRentalAmount;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("rowIndex", rowIndex)
            .add("leaseContractNumber", leaseContractNumber)
            .add("rentalSequenceNumber", rentalSequenceNumber)
            .add("leaseRentalDate", leaseRentalDate)
            .add("leaseRentalAmount", leaseRentalAmount)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractualLeaseRentalEVM)) return false;
        ContractualLeaseRentalEVM that = (ContractualLeaseRentalEVM) o;
        return getRowIndex() == that.getRowIndex() && Double.compare(that.getLeaseRentalAmount(), getLeaseRentalAmount()) == 0 && Objects.equal(getLeaseContractNumber(), that.getLeaseContractNumber()) && Objects.equal(getRentalSequenceNumber(), that.getRentalSequenceNumber()) && Objects.equal(getLeaseRentalDate(), that.getLeaseRentalDate());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRowIndex(), getLeaseContractNumber(), getRentalSequenceNumber(), getLeaseRentalDate(), getLeaseRentalAmount());
    }
}
