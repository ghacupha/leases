import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IContractualLeaseRental, ContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';
import { ContractualLeaseRentalService } from './contractual-lease-rental.service';
import { ContractualLeaseRentalComponent } from './contractual-lease-rental.component';
import { ContractualLeaseRentalDetailComponent } from './contractual-lease-rental-detail.component';
import { ContractualLeaseRentalUpdateComponent } from './contractual-lease-rental-update.component';

@Injectable({ providedIn: 'root' })
export class ContractualLeaseRentalResolve implements Resolve<IContractualLeaseRental> {
  constructor(private service: ContractualLeaseRentalService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContractualLeaseRental> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((contractualLeaseRental: HttpResponse<ContractualLeaseRental>) => {
          if (contractualLeaseRental.body) {
            return of(contractualLeaseRental.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContractualLeaseRental());
  }
}

export const contractualLeaseRentalRoute: Routes = [
  {
    path: '',
    component: ContractualLeaseRentalComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'ContractualLeaseRentals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractualLeaseRentalDetailComponent,
    resolve: {
      contractualLeaseRental: ContractualLeaseRentalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractualLeaseRentals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractualLeaseRentalUpdateComponent,
    resolve: {
      contractualLeaseRental: ContractualLeaseRentalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractualLeaseRentals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractualLeaseRentalUpdateComponent,
    resolve: {
      contractualLeaseRental: ContractualLeaseRentalResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'ContractualLeaseRentals',
    },
    canActivate: [UserRouteAccessService],
  },
];
