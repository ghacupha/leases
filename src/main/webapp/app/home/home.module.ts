import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LeasesSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class LeasesHomeModule {}
