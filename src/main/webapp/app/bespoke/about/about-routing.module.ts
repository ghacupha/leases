import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ABOUT_APP_ROUTE } from 'app/bespoke/about/about-app.route';
import { LeasesSharedModule } from 'app/shared/shared.module';

const routes: Routes = [ABOUT_APP_ROUTE];

@NgModule({
  imports: [LeasesSharedModule, RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AboutRoutingModule {}
