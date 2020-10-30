import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeaseDetails } from 'app/shared/model/lease-details.model';
import { LeaseDetailsService } from './lease-details.service';

@Component({
  templateUrl: './lease-details-delete-dialog.component.html',
})
export class LeaseDetailsDeleteDialogComponent {
  leaseDetails?: ILeaseDetails;

  constructor(
    protected leaseDetailsService: LeaseDetailsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaseDetailsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaseDetailsListModification');
      this.activeModal.close();
    });
  }
}
