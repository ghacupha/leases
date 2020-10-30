import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ILeasesFileUpload, LeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';
import { LeasesFileUploadService } from './leases-file-upload.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-leases-file-upload-update',
  templateUrl: './leases-file-upload-update.component.html',
})
export class LeasesFileUploadUpdateComponent implements OnInit {
  isSaving = false;
  periodFromDp: any;
  periodToDp: any;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    fileName: [null, [Validators.required]],
    periodFrom: [],
    periodTo: [],
    leasesFileTypeId: [null, [Validators.required]],
    dataFile: [null, [Validators.required]],
    dataFileContentType: [],
    uploadSuccessful: [],
    uploadProcessed: [],
    uploadToken: [null, []],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected leasesFileUploadService: LeasesFileUploadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesFileUpload }) => {
      this.updateForm(leasesFileUpload);
    });
  }

  updateForm(leasesFileUpload: ILeasesFileUpload): void {
    this.editForm.patchValue({
      id: leasesFileUpload.id,
      description: leasesFileUpload.description,
      fileName: leasesFileUpload.fileName,
      periodFrom: leasesFileUpload.periodFrom,
      periodTo: leasesFileUpload.periodTo,
      leasesFileTypeId: leasesFileUpload.leasesFileTypeId,
      dataFile: leasesFileUpload.dataFile,
      dataFileContentType: leasesFileUpload.dataFileContentType,
      uploadSuccessful: leasesFileUpload.uploadSuccessful,
      uploadProcessed: leasesFileUpload.uploadProcessed,
      uploadToken: leasesFileUpload.uploadToken,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('leasesApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leasesFileUpload = this.createFromForm();
    if (leasesFileUpload.id !== undefined) {
      this.subscribeToSaveResponse(this.leasesFileUploadService.update(leasesFileUpload));
    } else {
      this.subscribeToSaveResponse(this.leasesFileUploadService.create(leasesFileUpload));
    }
  }

  private createFromForm(): ILeasesFileUpload {
    return {
      ...new LeasesFileUpload(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      periodFrom: this.editForm.get(['periodFrom'])!.value,
      periodTo: this.editForm.get(['periodTo'])!.value,
      leasesFileTypeId: this.editForm.get(['leasesFileTypeId'])!.value,
      dataFileContentType: this.editForm.get(['dataFileContentType'])!.value,
      dataFile: this.editForm.get(['dataFile'])!.value,
      uploadSuccessful: this.editForm.get(['uploadSuccessful'])!.value,
      uploadProcessed: this.editForm.get(['uploadProcessed'])!.value,
      uploadToken: this.editForm.get(['uploadToken'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeasesFileUpload>>): void {
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
