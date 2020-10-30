import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ILeasesFileType } from 'app/shared/model/leases/leases-file-type.model';

@Component({
  selector: 'jhi-leases-file-type-detail',
  templateUrl: './leases-file-type-detail.component.html',
})
export class LeasesFileTypeDetailComponent implements OnInit {
  leasesFileType: ILeasesFileType | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesFileType }) => (this.leasesFileType = leasesFileType));
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
