import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICurrencyTable } from 'app/shared/model/leases/currency-table.model';

@Component({
  selector: 'jhi-currency-table-detail',
  templateUrl: './currency-table-detail.component.html',
})
export class CurrencyTableDetailComponent implements OnInit {
  currencyTable: ICurrencyTable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ currencyTable }) => (this.currencyTable = currencyTable));
  }

  previousState(): void {
    window.history.back();
  }
}
