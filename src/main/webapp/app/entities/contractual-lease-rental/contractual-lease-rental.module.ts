import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { ContractualLeaseRentalComponent } from './contractual-lease-rental.component';
import { ContractualLeaseRentalDetailComponent } from './contractual-lease-rental-detail.component';
import { ContractualLeaseRentalUpdateComponent } from './contractual-lease-rental-update.component';
import { ContractualLeaseRentalDeleteDialogComponent } from './contractual-lease-rental-delete-dialog.component';
import { contractualLeaseRentalRoute } from './contractual-lease-rental.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(contractualLeaseRentalRoute)],
  declarations: [
    ContractualLeaseRentalComponent,
    ContractualLeaseRentalDetailComponent,
    ContractualLeaseRentalUpdateComponent,
    ContractualLeaseRentalDeleteDialogComponent,
  ],
  entryComponents: [ContractualLeaseRentalDeleteDialogComponent],
})
export class LeasesContractualLeaseRentalModule {}
