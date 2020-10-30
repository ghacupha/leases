import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILeasesFileUpload, LeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';
import { LeasesFileUploadService } from './leases-file-upload.service';
import { LeasesFileUploadComponent } from './leases-file-upload.component';
import { LeasesFileUploadDetailComponent } from './leases-file-upload-detail.component';
import { LeasesFileUploadUpdateComponent } from './leases-file-upload-update.component';

@Injectable({ providedIn: 'root' })
export class LeasesFileUploadResolve implements Resolve<ILeasesFileUpload> {
  constructor(private service: LeasesFileUploadService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeasesFileUpload> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((leasesFileUpload: HttpResponse<LeasesFileUpload>) => {
          if (leasesFileUpload.body) {
            return of(leasesFileUpload.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeasesFileUpload());
  }
}

export const leasesFileUploadRoute: Routes = [
  {
    path: '',
    component: LeasesFileUploadComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LeasesFileUploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LeasesFileUploadDetailComponent,
    resolve: {
      leasesFileUpload: LeasesFileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileUploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LeasesFileUploadUpdateComponent,
    resolve: {
      leasesFileUpload: LeasesFileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileUploads',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LeasesFileUploadUpdateComponent,
    resolve: {
      leasesFileUpload: LeasesFileUploadResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LeasesFileUploads',
    },
    canActivate: [UserRouteAccessService],
  },
];
