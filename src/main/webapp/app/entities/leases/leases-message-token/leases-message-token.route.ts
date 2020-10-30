import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeasesMessageToken, LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';
import { LeasesMessageTokenService } from './leases-message-token.service';
import { LeasesMessageTokenComponent } from './leases-message-token.component';
import { LeasesMessageTokenDetailComponent } from './leases-message-token-detail.component';
import { LeasesMessageTokenUpdateComponent } from './leases-message-token-update.component';

@Injectable({ providedIn: 'root' })
export class LeasesMessageTokenResolve implements Resolve<ILeasesMessageToken> {
  constructor(private service: LeasesMessageTokenService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeasesMessageToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leasesMessageToken: HttpResponse<LeasesMessageToken>) => {
          if (leasesMessageToken.body) {
            return of(leasesMessageToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeasesMessageToken());
  }
}

export const leasesMessageTokenRoute: Routes = [
  {
    path: '',
    component: LeasesMessageTokenComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LeasesMessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeasesMessageTokenDetailComponent,
    resolve: {
      leasesMessageToken: LeasesMessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesMessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeasesMessageTokenUpdateComponent,
    resolve: {
      leasesMessageToken: LeasesMessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesMessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeasesMessageTokenUpdateComponent,
    resolve: {
      leasesMessageToken: LeasesMessageTokenResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesMessageTokens',
    },
    canActivate: [UserRouteAccessService],
  },
];
