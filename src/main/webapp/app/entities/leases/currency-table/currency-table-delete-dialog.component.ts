import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICurrencyTable } from 'app/shared/model/leases/currency-table.model';
import { CurrencyTableService } from './currency-table.service';

@Component({
  templateUrl: './currency-table-delete-dialog.component.html',
})
export class CurrencyTableDeleteDialogComponent {
  currencyTable?: ICurrencyTable;

  constructor(
    protected currencyTableService: CurrencyTableService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.currencyTableService.delete(id).subscribe(() => {
      this.eventManager.broadcast('currencyTableListModification');
      this.activeModal.close();
    });
  }
}
