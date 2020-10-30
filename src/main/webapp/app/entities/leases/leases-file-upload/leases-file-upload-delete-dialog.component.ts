import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';
import { LeasesFileUploadService } from './leases-file-upload.service';

@Component({
  templateUrl: './leases-file-upload-delete-dialog.component.html',
})
export class LeasesFileUploadDeleteDialogComponent {
  leasesFileUpload?: ILeasesFileUpload;

  constructor(
    protected leasesFileUploadService: LeasesFileUploadService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leasesFileUploadService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leasesFileUploadListModification');
      this.activeModal.close();
    });
  }
}
