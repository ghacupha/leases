import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { CurrencyTableComponent } from './currency-table.component';
import { CurrencyTableDetailComponent } from './currency-table-detail.component';
import { CurrencyTableUpdateComponent } from './currency-table-update.component';
import { CurrencyTableDeleteDialogComponent } from './currency-table-delete-dialog.component';
import { currencyTableRoute } from './currency-table.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(currencyTableRoute)],
  declarations: [CurrencyTableComponent, CurrencyTableDetailComponent, CurrencyTableUpdateComponent, CurrencyTableDeleteDialogComponent],
  entryComponents: [CurrencyTableDeleteDialogComponent],
})
export class LeasesCurrencyTableModule {}
