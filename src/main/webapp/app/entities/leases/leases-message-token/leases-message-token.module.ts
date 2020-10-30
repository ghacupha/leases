import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { LeasesMessageTokenComponent } from './leases-message-token.component';
import { LeasesMessageTokenDetailComponent } from './leases-message-token-detail.component';
import { LeasesMessageTokenUpdateComponent } from './leases-message-token-update.component';
import { LeasesMessageTokenDeleteDialogComponent } from './leases-message-token-delete-dialog.component';
import { leasesMessageTokenRoute } from './leases-message-token.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(leasesMessageTokenRoute)],
  declarations: [
    LeasesMessageTokenComponent,
    LeasesMessageTokenDetailComponent,
    LeasesMessageTokenUpdateComponent,
    LeasesMessageTokenDeleteDialogComponent,
  ],
  entryComponents: [LeasesMessageTokenDeleteDialogComponent],
})
export class LeasesLeasesMessageTokenModule {}
