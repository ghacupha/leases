package io.github.leases.internal.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

import java.io.Serializable;

/**
 * Excel view model for lease-details
 */
public class LeaseDetailsEVM implements Serializable {

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String leaseContractNumber;

    @ExcelCell(1)
    private double incrementalBorrowingRate;

    @ExcelCell(2)
    private String commencementDate;

    @ExcelCell(3)
    private double leasePrepayments;

    @ExcelCell(4)
    private double initialDirectCosts;

    @ExcelCell(5)
    private double demolitionCosts;

    @ExcelCell(6)
    private String assetAccountNumber;

    @ExcelCell(7)
    private String liabilityAccountNumber;

    @ExcelCell(8)
    private String depreciationAccountNumber;

    @ExcelCell(9)
    private String interestAccountNumber;

    public LeaseDetailsEVM(int rowIndex, String leaseContractNumber, double incrementalBorrowingRate, String commencementDate, double leasePrepayments, double initialDirectCosts, double demolitionCosts, String assetAccountNumber, String liabilityAccountNumber, String depreciationAccountNumber, String interestAccountNumber) {
        this.rowIndex = rowIndex;
        this.leaseContractNumber = leaseContractNumber;
        this.incrementalBorrowingRate = incrementalBorrowingRate;
        this.commencementDate = commencementDate;
        this.leasePrepayments = leasePrepayments;
        this.initialDirectCosts = initialDirectCosts;
        this.demolitionCosts = demolitionCosts;
        this.assetAccountNumber = assetAccountNumber;
        this.liabilityAccountNumber = liabilityAccountNumber;
        this.depreciationAccountNumber = depreciationAccountNumber;
        this.interestAccountNumber = interestAccountNumber;
    }

    public LeaseDetailsEVM() {
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public LeaseDetailsEVM setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public String getLeaseContractNumber() {
        return leaseContractNumber;
    }

    public LeaseDetailsEVM setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
        return this;
    }

    public double getIncrementalBorrowingRate() {
        return incrementalBorrowingRate;
    }

    public LeaseDetailsEVM setIncrementalBorrowingRate(double incrementalBorrowingRate) {
        this.incrementalBorrowingRate = incrementalBorrowingRate;
        return this;
    }

    public String getCommencementDate() {
        return commencementDate;
    }

    public LeaseDetailsEVM setCommencementDate(String commencementDate) {
        this.commencementDate = commencementDate;
        return this;
    }

    public double getLeasePrepayments() {
        return leasePrepayments;
    }

    public LeaseDetailsEVM setLeasePrepayments(double leasePrepayments) {
        this.leasePrepayments = leasePrepayments;
        return this;
    }

    public double getInitialDirectCosts() {
        return initialDirectCosts;
    }

    public LeaseDetailsEVM setInitialDirectCosts(double initialDirectCosts) {
        this.initialDirectCosts = initialDirectCosts;
        return this;
    }

    public double getDemolitionCosts() {
        return demolitionCosts;
    }

    public LeaseDetailsEVM setDemolitionCosts(double demolitionCosts) {
        this.demolitionCosts = demolitionCosts;
        return this;
    }

    public String getAssetAccountNumber() {
        return assetAccountNumber;
    }

    public LeaseDetailsEVM setAssetAccountNumber(String assetAccountNumber) {
        this.assetAccountNumber = assetAccountNumber;
        return this;
    }

    public String getLiabilityAccountNumber() {
        return liabilityAccountNumber;
    }

    public LeaseDetailsEVM setLiabilityAccountNumber(String liabilityAccountNumber) {
        this.liabilityAccountNumber = liabilityAccountNumber;
        return this;
    }

    public String getDepreciationAccountNumber() {
        return depreciationAccountNumber;
    }

    public LeaseDetailsEVM setDepreciationAccountNumber(String depreciationAccountNumber) {
        this.depreciationAccountNumber = depreciationAccountNumber;
        return this;
    }

    public String getInterestAccountNumber() {
        return interestAccountNumber;
    }

    public LeaseDetailsEVM setInterestAccountNumber(String interestAccountNumber) {
        this.interestAccountNumber = interestAccountNumber;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("rowIndex", rowIndex)
            .add("leaseContractNumber", leaseContractNumber)
            .add("incrementalBorrowingRate", incrementalBorrowingRate)
            .add("commencementDate", commencementDate)
            .add("leasePrepayments", leasePrepayments)
            .add("initialDirectCosts", initialDirectCosts)
            .add("demolitionCosts", demolitionCosts)
            .add("assetAccountNumber", assetAccountNumber)
            .add("liabilityAccountNumber", liabilityAccountNumber)
            .add("depreciationAccountNumber", depreciationAccountNumber)
            .add("interestAccountNumber", interestAccountNumber)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaseDetailsEVM)) return false;
        LeaseDetailsEVM that = (LeaseDetailsEVM) o;
        return getRowIndex() == that.getRowIndex() && Double.compare(that.getIncrementalBorrowingRate(), getIncrementalBorrowingRate()) == 0 && Double.compare(that.getLeasePrepayments(), getLeasePrepayments()) == 0 && Double.compare(that.getInitialDirectCosts(), getInitialDirectCosts()) == 0 && Double.compare(that.getDemolitionCosts(), getDemolitionCosts()) == 0 && Objects.equal(getLeaseContractNumber(), that.getLeaseContractNumber()) && Objects.equal(getCommencementDate(), that.getCommencementDate()) && Objects.equal(getAssetAccountNumber(), that.getAssetAccountNumber()) && Objects.equal(getLiabilityAccountNumber(), that.getLiabilityAccountNumber()) && Objects.equal(getDepreciationAccountNumber(), that.getDepreciationAccountNumber()) && Objects.equal(getInterestAccountNumber(), that.getInterestAccountNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRowIndex(), getLeaseContractNumber(), getIncrementalBorrowingRate(), getCommencementDate(), getLeasePrepayments(), getInitialDirectCosts(), getDemolitionCosts(), getAssetAccountNumber(), getLiabilityAccountNumber(), getDepreciationAccountNumber(), getInterestAccountNumber());
    }
}
