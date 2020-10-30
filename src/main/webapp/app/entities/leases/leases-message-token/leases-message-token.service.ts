import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ILeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

type EntityResponseType = HttpResponse<ILeasesMessageToken>;
type EntityArrayResponseType = HttpResponse<ILeasesMessageToken[]>;

@Injectable({ providedIn: 'root' })
export class LeasesMessageTokenService {
  public resourceUrl = SERVER_API_URL + 'api/leases-message-tokens';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/leases-message-tokens';

  constructor(protected http: HttpClient) {}

  create(leasesMessageToken: ILeasesMessageToken): Observable<EntityResponseType> {
    return this.http.post<ILeasesMessageToken>(this.resourceUrl, leasesMessageToken, { observe: 'response' });
  }

  update(leasesMessageToken: ILeasesMessageToken): Observable<EntityResponseType> {
    return this.http.put<ILeasesMessageToken>(this.resourceUrl, leasesMessageToken, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeasesMessageToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeasesMessageToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeasesMessageToken[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
