import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { LeasesSharedModule } from 'app/shared/shared.module';
import { LeasesCoreModule } from 'app/core/core.module';
import { LeasesAppRoutingModule } from './app-routing.module';
import { LeasesHomeModule } from './home/home.module';
import { LeasesEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    LeasesSharedModule,
    LeasesCoreModule,
    LeasesHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    LeasesEntityModule,
    LeasesAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class LeasesAppModule {}
