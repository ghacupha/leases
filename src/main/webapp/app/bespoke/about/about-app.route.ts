import { Route } from '@angular/router';
import { AboutComponent } from 'app/bespoke/about/about.component';

/**
 * Route for the about component
 */
export const ABOUT_APP_ROUTE: Route = {
  path: 'leases',
  component: AboutComponent,
  data: {
    authorities: [],
    pageTitle: 'Leases Management System',
  },
};
