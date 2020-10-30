import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeaseDetails, LeaseDetails } from 'app/shared/model/lease-details.model';
import { LeaseDetailsService } from './lease-details.service';

@Component({
  selector: 'jhi-lease-details-update',
  templateUrl: './lease-details-update.component.html',
})
export class LeaseDetailsUpdateComponent implements OnInit {
  isSaving = false;
  commencementDateDp: any;

  editForm = this.fb.group({
    id: [],
    leaseContractNumber: [null, [Validators.required]],
    incrementalBorrowingRate: [],
    commencementDate: [],
    leasePrepayments: [],
    initialDirectCosts: [],
    demolitionCosts: [],
    assetAccountNumber: [],
    liabilityAccountNumber: [],
    depreciationAccountNumber: [],
    interestAccountNumber: [],
  });

  constructor(protected leaseDetailsService: LeaseDetailsService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseDetails }) => {
      this.updateForm(leaseDetails);
    });
  }

  updateForm(leaseDetails: ILeaseDetails): void {
    this.editForm.patchValue({
      id: leaseDetails.id,
      leaseContractNumber: leaseDetails.leaseContractNumber,
      incrementalBorrowingRate: leaseDetails.incrementalBorrowingRate,
      commencementDate: leaseDetails.commencementDate,
      leasePrepayments: leaseDetails.leasePrepayments,
      initialDirectCosts: leaseDetails.initialDirectCosts,
      demolitionCosts: leaseDetails.demolitionCosts,
      assetAccountNumber: leaseDetails.assetAccountNumber,
      liabilityAccountNumber: leaseDetails.liabilityAccountNumber,
      depreciationAccountNumber: leaseDetails.depreciationAccountNumber,
      interestAccountNumber: leaseDetails.interestAccountNumber,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaseDetails = this.createFromForm();
    if (leaseDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.leaseDetailsService.update(leaseDetails));
    } else {
      this.subscribeToSaveResponse(this.leaseDetailsService.create(leaseDetails));
    }
  }

  private createFromForm(): ILeaseDetails {
    return {
      ...new LeaseDetails(),
      id: this.editForm.get(['id'])!.value,
      leaseContractNumber: this.editForm.get(['leaseContractNumber'])!.value,
      incrementalBorrowingRate: this.editForm.get(['incrementalBorrowingRate'])!.value,
      commencementDate: this.editForm.get(['commencementDate'])!.value,
      leasePrepayments: this.editForm.get(['leasePrepayments'])!.value,
      initialDirectCosts: this.editForm.get(['initialDirectCosts'])!.value,
      demolitionCosts: this.editForm.get(['demolitionCosts'])!.value,
      assetAccountNumber: this.editForm.get(['assetAccountNumber'])!.value,
      liabilityAccountNumber: this.editForm.get(['liabilityAccountNumber'])!.value,
      depreciationAccountNumber: this.editForm.get(['depreciationAccountNumber'])!.value,
      interestAccountNumber: this.editForm.get(['interestAccountNumber'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaseDetails>>): void {
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
