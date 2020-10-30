import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AboutRoutingModule } from './about-routing.module';
import { AboutComponent } from './about.component';

/**
 * This module contains component for display of the about page
 */
@NgModule({
  declarations: [AboutComponent],
  imports: [CommonModule, AboutRoutingModule],
  exports: [AboutComponent],
  entryComponents: [AboutComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AboutModule {}
