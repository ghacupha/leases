import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { LeasesFileTypeComponent } from './leases-file-type.component';
import { LeasesFileTypeDetailComponent } from './leases-file-type-detail.component';
import { LeasesFileTypeUpdateComponent } from './leases-file-type-update.component';
import { LeasesFileTypeDeleteDialogComponent } from './leases-file-type-delete-dialog.component';
import { leasesFileTypeRoute } from './leases-file-type.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(leasesFileTypeRoute)],
  declarations: [
    LeasesFileTypeComponent,
    LeasesFileTypeDetailComponent,
    LeasesFileTypeUpdateComponent,
    LeasesFileTypeDeleteDialogComponent,
  ],
  entryComponents: [LeasesFileTypeDeleteDialogComponent],
})
export class LeasesLeasesFileTypeModule {}
