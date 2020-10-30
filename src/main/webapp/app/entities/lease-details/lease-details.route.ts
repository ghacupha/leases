import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeaseDetails, LeaseDetails } from 'app/shared/model/lease-details.model';
import { LeaseDetailsService } from './lease-details.service';
import { LeaseDetailsComponent } from './lease-details.component';
import { LeaseDetailsDetailComponent } from './lease-details-detail.component';
import { LeaseDetailsUpdateComponent } from './lease-details-update.component';

@Injectable({ providedIn: 'root' })
export class LeaseDetailsResolve implements Resolve<ILeaseDetails> {
  constructor(private service: LeaseDetailsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leaseDetails: HttpResponse<LeaseDetails>) => {
          if (leaseDetails.body) {
            return of(leaseDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseDetails());
  }
}

export const leaseDetailsRoute: Routes = [
  {
    path: '',
    component: LeaseDetailsComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LeaseDetails',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeaseDetailsDetailComponent,
    resolve: {
      leaseDetails: LeaseDetailsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaseDetails',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeaseDetailsUpdateComponent,
    resolve: {
      leaseDetails: LeaseDetailsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaseDetails',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeaseDetailsUpdateComponent,
    resolve: {
      leaseDetails: LeaseDetailsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeaseDetails',
    },
    canActivate: [UserRouteAccessService],
  },
];
