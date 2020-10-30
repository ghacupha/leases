import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IContractualLeaseRental, ContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';
import { ContractualLeaseRentalService } from './contractual-lease-rental.service';

@Component({
  selector: 'jhi-contractual-lease-rental-update',
  templateUrl: './contractual-lease-rental-update.component.html',
})
export class ContractualLeaseRentalUpdateComponent implements OnInit {
  isSaving = false;
  leaseRentalDateDp: any;

  editForm = this.fb.group({
    id: [],
    leaseContractNumber: [null, [Validators.required]],
    rentalSequenceNumber: [null, [Validators.required]],
    leaseRentalDate: [],
    leaseRentalAmount: [],
  });

  constructor(
    protected contractualLeaseRentalService: ContractualLeaseRentalService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractualLeaseRental }) => {
      this.updateForm(contractualLeaseRental);
    });
  }

  updateForm(contractualLeaseRental: IContractualLeaseRental): void {
    this.editForm.patchValue({
      id: contractualLeaseRental.id,
      leaseContractNumber: contractualLeaseRental.leaseContractNumber,
      rentalSequenceNumber: contractualLeaseRental.rentalSequenceNumber,
      leaseRentalDate: contractualLeaseRental.leaseRentalDate,
      leaseRentalAmount: contractualLeaseRental.leaseRentalAmount,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contractualLeaseRental = this.createFromForm();
    if (contractualLeaseRental.id !== undefined) {
      this.subscribeToSaveResponse(this.contractualLeaseRentalService.update(contractualLeaseRental));
    } else {
      this.subscribeToSaveResponse(this.contractualLeaseRentalService.create(contractualLeaseRental));
    }
  }

  private createFromForm(): IContractualLeaseRental {
    return {
      ...new ContractualLeaseRental(),
      id: this.editForm.get(['id'])!.value,
      leaseContractNumber: this.editForm.get(['leaseContractNumber'])!.value,
      rentalSequenceNumber: this.editForm.get(['rentalSequenceNumber'])!.value,
      leaseRentalDate: this.editForm.get(['leaseRentalDate'])!.value,
      leaseRentalAmount: this.editForm.get(['leaseRentalAmount'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContractualLeaseRental>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
