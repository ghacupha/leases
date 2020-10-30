import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { LeasesFileUploadComponent } from './leases-file-upload.component';
import { LeasesFileUploadDetailComponent } from './leases-file-upload-detail.component';
import { LeasesFileUploadUpdateComponent } from './leases-file-upload-update.component';
import { LeasesFileUploadDeleteDialogComponent } from './leases-file-upload-delete-dialog.component';
import { leasesFileUploadRoute } from './leases-file-upload.route';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(leasesFileUploadRoute)],
  declarations: [
    LeasesFileUploadComponent,
    LeasesFileUploadDetailComponent,
    LeasesFileUploadUpdateComponent,
    LeasesFileUploadDeleteDialogComponent,
  ],
  entryComponents: [LeasesFileUploadDeleteDialogComponent],
})
export class LeasesLeasesFileUploadModule {}
