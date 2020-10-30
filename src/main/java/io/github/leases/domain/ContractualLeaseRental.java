package io.github.leases.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A ContractualLeaseRental.
 */
@Entity
@Table(name = "contractual_lease_rental")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contractualleaserental")
public class ContractualLeaseRental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "lease_contract_number", nullable = false)
    private String leaseContractNumber;

    @NotNull
    @Column(name = "rental_sequence_number", nullable = false)
    private Integer rentalSequenceNumber;

    @Column(name = "lease_rental_date")
    private LocalDate leaseRentalDate;

    @Column(name = "lease_rental_amount", precision = 21, scale = 2)
    private BigDecimal leaseRentalAmount;

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

    public ContractualLeaseRental leaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
        return this;
    }

    public void setLeaseContractNumber(String leaseContractNumber) {
        this.leaseContractNumber = leaseContractNumber;
    }

    public Integer getRentalSequenceNumber() {
        return rentalSequenceNumber;
    }

    public ContractualLeaseRental rentalSequenceNumber(Integer rentalSequenceNumber) {
        this.rentalSequenceNumber = rentalSequenceNumber;
        return this;
    }

    public void setRentalSequenceNumber(Integer rentalSequenceNumber) {
        this.rentalSequenceNumber = rentalSequenceNumber;
    }

    public LocalDate getLeaseRentalDate() {
        return leaseRentalDate;
    }

    public ContractualLeaseRental leaseRentalDate(LocalDate leaseRentalDate) {
        this.leaseRentalDate = leaseRentalDate;
        return this;
    }

    public void setLeaseRentalDate(LocalDate leaseRentalDate) {
        this.leaseRentalDate = leaseRentalDate;
    }

    public BigDecimal getLeaseRentalAmount() {
        return leaseRentalAmount;
    }

    public ContractualLeaseRental leaseRentalAmount(BigDecimal leaseRentalAmount) {
        this.leaseRentalAmount = leaseRentalAmount;
        return this;
    }

    public void setLeaseRentalAmount(BigDecimal leaseRentalAmount) {
        this.leaseRentalAmount = leaseRentalAmount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractualLeaseRental)) {
            return false;
        }
        return id != null && id.equals(((ContractualLeaseRental) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractualLeaseRental{" +
            "id=" + getId() +
            ", leaseContractNumber='" + getLeaseContractNumber() + "'" +
            ", rentalSequenceNumber=" + getRentalSequenceNumber() +
            ", leaseRentalDate='" + getLeaseRentalDate() + "'" +
            ", leaseRentalAmount=" + getLeaseRentalAmount() +
            "}";
    }
}
