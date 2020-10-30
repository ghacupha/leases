import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';
import { LeasesMessageTokenService } from './leases-message-token.service';

@Component({
  templateUrl: './leases-message-token-delete-dialog.component.html',
})
export class LeasesMessageTokenDeleteDialogComponent {
  leasesMessageToken?: ILeasesMessageToken;

  constructor(
    protected leasesMessageTokenService: LeasesMessageTokenService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leasesMessageTokenService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leasesMessageTokenListModification');
      this.activeModal.close();
    });
  }
}
