import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ICurrencyTable } from 'app/shared/model/leases/currency-table.model';

type EntityResponseType = HttpResponse<ICurrencyTable>;
type EntityArrayResponseType = HttpResponse<ICurrencyTable[]>;

@Injectable({ providedIn: 'root' })
export class CurrencyTableService {
  public resourceUrl = SERVER_API_URL + 'api/currency-tables';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/currency-tables';

  constructor(protected http: HttpClient) {}

  create(currencyTable: ICurrencyTable): Observable<EntityResponseType> {
    return this.http.post<ICurrencyTable>(this.resourceUrl, currencyTable, { observe: 'response' });
  }

  update(currencyTable: ICurrencyTable): Observable<EntityResponseType> {
    return this.http.put<ICurrencyTable>(this.resourceUrl, currencyTable, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICurrencyTable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrencyTable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICurrencyTable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
