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
      {
        path: 'leases-file-type',
        loadChildren: () => import('./leases/leases-file-type/leases-file-type.module').then(m => m.LeasesLeasesFileTypeModule),
      },
      {
        path: 'leases-file-upload',
        loadChildren: () => import('./leases/leases-file-upload/leases-file-upload.module').then(m => m.LeasesLeasesFileUploadModule),
      },
      {
        path: 'leases-message-token',
        loadChildren: () => import('./leases/leases-message-token/leases-message-token.module').then(m => m.LeasesLeasesMessageTokenModule),
      },
      {
        path: 'currency-table',
        loadChildren: () => import('./leases/currency-table/currency-table.module').then(m => m.LeasesCurrencyTableModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LeasesEntityModule {}
