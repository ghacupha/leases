import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AboutModule } from './about/about.module';
import { RouterModule, Routes } from '@angular/router';
import { BespokeNavigationModule } from 'app/bespoke/bespoke-navigation/bespoke-navigation.module';
import { LeasesSharedModule } from 'app/shared/shared.module';

const routes: Routes = [
  {
    path: 'about',
    loadChildren: () => import('./about/about.module').then(m => m.AboutModule),
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, LeasesSharedModule, RouterModule.forChild(routes), BespokeNavigationModule, AboutModule],
  exports: [RouterModule, BespokeNavigationModule, AboutModule],
})
export class BespokeModule {}
