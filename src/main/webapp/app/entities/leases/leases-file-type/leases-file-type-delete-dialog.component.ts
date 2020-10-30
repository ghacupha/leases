import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeasesFileType } from 'app/shared/model/leases/leases-file-type.model';
import { LeasesFileTypeService } from './leases-file-type.service';

@Component({
  templateUrl: './leases-file-type-delete-dialog.component.html',
})
export class LeasesFileTypeDeleteDialogComponent {
  leasesFileType?: ILeasesFileType;

  constructor(
    protected leasesFileTypeService: LeasesFileTypeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leasesFileTypeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leasesFileTypeListModification');
      this.activeModal.close();
    });
  }
}
