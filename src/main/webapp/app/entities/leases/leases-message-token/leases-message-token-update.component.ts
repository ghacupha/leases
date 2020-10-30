import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILeasesMessageToken, LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';
import { LeasesMessageTokenService } from './leases-message-token.service';

@Component({
  selector: 'jhi-leases-message-token-update',
  templateUrl: './leases-message-token-update.component.html',
})
export class LeasesMessageTokenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [],
    timeSent: [null, [Validators.required]],
    tokenValue: [null, [Validators.required]],
    received: [],
    actioned: [],
    contentFullyEnqueued: [],
  });

  constructor(
    protected leasesMessageTokenService: LeasesMessageTokenService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ leasesMessageToken }) => {
      this.updateForm(leasesMessageToken);
    });
  }

  updateForm(leasesMessageToken: ILeasesMessageToken): void {
    this.editForm.patchValue({
      id: leasesMessageToken.id,
      description: leasesMessageToken.description,
      timeSent: leasesMessageToken.timeSent,
      tokenValue: leasesMessageToken.tokenValue,
      received: leasesMessageToken.received,
      actioned: leasesMessageToken.actioned,
      contentFullyEnqueued: leasesMessageToken.contentFullyEnqueued,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leasesMessageToken = this.createFromForm();
    if (leasesMessageToken.id !== undefined) {
      this.subscribeToSaveResponse(this.leasesMessageTokenService.update(leasesMessageToken));
    } else {
      this.subscribeToSaveResponse(this.leasesMessageTokenService.create(leasesMessageToken));
    }
  }

  private createFromForm(): ILeasesMessageToken {
    return {
      ...new LeasesMessageToken(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      timeSent: this.editForm.get(['timeSent'])!.value,
      tokenValue: this.editForm.get(['tokenValue'])!.value,
      received: this.editForm.get(['received'])!.value,
      actioned: this.editForm.get(['actioned'])!.value,
      contentFullyEnqueued: this.editForm.get(['contentFullyEnqueued'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeasesMessageToken>>): void {
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
