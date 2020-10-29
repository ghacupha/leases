///
/// Leases - Leases management application
/// Copyright © 2020 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { createRequestOption, Pagination } from 'app/shared/util/request-util';
import { SERVER_API_URL } from 'app/app.constants';
import { Audit } from './audit.model';

export interface AuditsQuery extends Pagination {
  fromDate: string;
  toDate: string;
}

@Injectable({ providedIn: 'root' })
export class AuditsService {
  constructor(private http: HttpClient) {}

  query(req: AuditsQuery): Observable<HttpResponse<Audit[]>> {
    const params: HttpParams = createRequestOption(req);

    const requestURL = SERVER_API_URL + 'management/audits';

    return this.http.get<Audit[]>(requestURL, {
      params,
      observe: 'response',
    });
  }
}
