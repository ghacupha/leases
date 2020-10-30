import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeasesFileType, LeasesFileType } from 'app/shared/model/leases/leases-file-type.model';
import { LeasesFileTypeService } from './leases-file-type.service';
import { LeasesFileTypeComponent } from './leases-file-type.component';
import { LeasesFileTypeDetailComponent } from './leases-file-type-detail.component';
import { LeasesFileTypeUpdateComponent } from './leases-file-type-update.component';

@Injectable({ providedIn: 'root' })
export class LeasesFileTypeResolve implements Resolve<ILeasesFileType> {
  constructor(private service: LeasesFileTypeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeasesFileType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leasesFileType: HttpResponse<LeasesFileType>) => {
          if (leasesFileType.body) {
            return of(leasesFileType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeasesFileType());
  }
}

export const leasesFileTypeRoute: Routes = [
  {
    path: '',
    component: LeasesFileTypeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LeasesFileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeasesFileTypeDetailComponent,
    resolve: {
      leasesFileType: LeasesFileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeasesFileTypeUpdateComponent,
    resolve: {
      leasesFileType: LeasesFileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeasesFileTypeUpdateComponent,
    resolve: {
      leasesFileType: LeasesFileTypeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileTypes',
    },
    canActivate: [UserRouteAccessService],
  },
];
