import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ILeasesFileType } from 'app/shared/model/leases/leases-file-type.model';

type EntityResponseType = HttpResponse<ILeasesFileType>;
type EntityArrayResponseType = HttpResponse<ILeasesFileType[]>;

@Injectable({ providedIn: 'root' })
export class LeasesFileTypeService {
  public resourceUrl = SERVER_API_URL + 'api/leases-file-types';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/leases-file-types';

  constructor(protected http: HttpClient) {}

  create(leasesFileType: ILeasesFileType): Observable<EntityResponseType> {
    return this.http.post<ILeasesFileType>(this.resourceUrl, leasesFileType, { observe: 'response' });
  }

  update(leasesFileType: ILeasesFileType): Observable<EntityResponseType> {
    return this.http.put<ILeasesFileType>(this.resourceUrl, leasesFileType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeasesFileType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeasesFileType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeasesFileType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
