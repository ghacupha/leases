import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';

@Component({
  selector: 'jhi-contractual-lease-rental-detail',
  templateUrl: './contractual-lease-rental-detail.component.html',
})
export class ContractualLeaseRentalDetailComponent implements OnInit {
  contractualLeaseRental: IContractualLeaseRental | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contractualLeaseRental }) => (this.contractualLeaseRental = contractualLeaseRental));
  }

  previousState(): void {
    window.history.back();
  }
}
