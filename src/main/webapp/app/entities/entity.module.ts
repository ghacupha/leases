import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'contractual-lease-rental',
        loadChildren: () =>
          import('./contractual-lease-rental/contractual-lease-rental.module').then(m => m.LeasesContractualLeaseRentalModule),
      },
      {
        path: 'lease-details',
        loadChildren: () => import('./lease-details/lease-details.module').then(m => m.LeasesLeaseDetailsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LeasesEntityModule {}
