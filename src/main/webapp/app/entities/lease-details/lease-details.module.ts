import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { LeaseDetailsComponent } from './lease-details.component';
import { LeaseDetailsDetailComponent } from './lease-details-detail.component';
import { LeaseDetailsUpdateComponent } from './lease-details-update.component';
import { LeaseDetailsDeleteDialogComponent } from './lease-details-delete-dialog.component';
import { leaseDetailsRoute } from './lease-details.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(leaseDetailsRoute)],
  declarations: [LeaseDetailsComponent, LeaseDetailsDetailComponent, LeaseDetailsUpdateComponent, LeaseDetailsDeleteDialogComponent],
  entryComponents: [LeaseDetailsDeleteDialogComponent],
})
export class LeasesLeaseDetailsModule {}
