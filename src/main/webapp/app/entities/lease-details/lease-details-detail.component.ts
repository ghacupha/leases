import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILeaseDetails } from 'app/shared/model/lease-details.model';

@Component({
  selector: 'jhi-lease-details-detail',
  templateUrl: './lease-details-detail.component.html',
})
export class LeaseDetailsDetailComponent implements OnInit {
  leaseDetails: ILeaseDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leaseDetails }) => (this.leaseDetails = leaseDetails));
  }

  previousState(): void {
    window.history.back();
  }
}
