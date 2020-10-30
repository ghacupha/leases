import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ILeasesFileType, LeasesFileType } from 'app/shared/model/leases/leases-file-type.model';
import { LeasesFileTypeService } from './leases-file-type.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-leases-file-type-update',
  templateUrl: './leases-file-type-update.component.html',
})
export class LeasesFileTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    leasesFileTypeName: [null, [Validators.required]],
    leasesFileMediumType: [null, [Validators.required]],
    description: [],
    fileTemplate: [],
    fileTemplateContentType: [],
    leasesfileType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected leasesFileTypeService: LeasesFileTypeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesFileType }) => {
      this.updateForm(leasesFileType);
    });
  }

  updateForm(leasesFileType: ILeasesFileType): void {
    this.editForm.patchValue({
      id: leasesFileType.id,
      leasesFileTypeName: leasesFileType.leasesFileTypeName,
      leasesFileMediumType: leasesFileType.leasesFileMediumType,
      description: leasesFileType.description,
      fileTemplate: leasesFileType.fileTemplate,
      fileTemplateContentType: leasesFileType.fileTemplateContentType,
      leasesfileType: leasesFileType.leasesfileType,
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
    const leasesFileType = this.createFromForm();
    if (leasesFileType.id !== undefined) {
      this.subscribeToSaveResponse(this.leasesFileTypeService.update(leasesFileType));
    } else {
      this.subscribeToSaveResponse(this.leasesFileTypeService.create(leasesFileType));
    }
  }

  private createFromForm(): ILeasesFileType {
    return {
      ...new LeasesFileType(),
      id: this.editForm.get(['id'])!.value,
      leasesFileTypeName: this.editForm.get(['leasesFileTypeName'])!.value,
      leasesFileMediumType: this.editForm.get(['leasesFileMediumType'])!.value,
      description: this.editForm.get(['description'])!.value,
      fileTemplateContentType: this.editForm.get(['fileTemplateContentType'])!.value,
      fileTemplate: this.editForm.get(['fileTemplate'])!.value,
      leasesfileType: this.editForm.get(['leasesfileType'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeasesFileType>>): void {
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
