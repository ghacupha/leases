import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ElocoSharedModule } from 'app/shared/shared.module';
import { ElocoCoreModule } from 'app/core/core.module';
import { ElocoAppRoutingModule } from './app-routing.module';
import { ElocoHomeModule } from './home/home.module';
import { ElocoEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ElocoSharedModule,
    ElocoCoreModule,
    ElocoHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ElocoEntityModule,
    ElocoAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ElocoAppModule {}
