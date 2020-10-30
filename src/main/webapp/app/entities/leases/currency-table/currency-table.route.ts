import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICurrencyTable, CurrencyTable } from 'app/shared/model/leases/currency-table.model';
import { CurrencyTableService } from './currency-table.service';
import { CurrencyTableComponent } from './currency-table.component';
import { CurrencyTableDetailComponent } from './currency-table-detail.component';
import { CurrencyTableUpdateComponent } from './currency-table-update.component';

@Injectable({ providedIn: 'root' })
export class CurrencyTableResolve implements Resolve<ICurrencyTable> {
  constructor(private service: CurrencyTableService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrencyTable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((currencyTable: HttpResponse<CurrencyTable>) => {
          if (currencyTable.body) {
            return of(currencyTable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CurrencyTable());
  }
}

export const currencyTableRoute: Routes = [
  {
    path: '',
    component: CurrencyTableComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'CurrencyTables',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurrencyTableDetailComponent,
    resolve: {
      currencyTable: CurrencyTableResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CurrencyTables',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CurrencyTableUpdateComponent,
    resolve: {
      currencyTable: CurrencyTableResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CurrencyTables',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CurrencyTableUpdateComponent,
    resolve: {
      currencyTable: CurrencyTableResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'CurrencyTables',
    },
    canActivate: [UserRouteAccessService],
  },
];
