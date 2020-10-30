import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ILeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';

@Component({
  selector: 'jhi-leases-file-upload-detail',
  templateUrl: './leases-file-upload-detail.component.html',
})
export class LeasesFileUploadDetailComponent implements OnInit {
  leasesFileUpload: ILeasesFileUpload | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesFileUpload }) => (this.leasesFileUpload = leasesFileUpload));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
