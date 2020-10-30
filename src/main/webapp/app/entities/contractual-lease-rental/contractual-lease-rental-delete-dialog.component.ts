import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';
import { ContractualLeaseRentalService } from './contractual-lease-rental.service';

@Component({
  templateUrl: './contractual-lease-rental-delete-dialog.component.html',
})
export class ContractualLeaseRentalDeleteDialogComponent {
  contractualLeaseRental?: IContractualLeaseRental;

  constructor(
    protected contractualLeaseRentalService: ContractualLeaseRentalService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractualLeaseRentalService.delete(id).subscribe(() => {
      this.eventManager.broadcast('contractualLeaseRentalListModification');
      this.activeModal.close();
    });
  }
}
