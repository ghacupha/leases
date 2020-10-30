import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ILeaseDetails } from 'app/shared/model/lease-details.model';

type EntityResponseType = HttpResponse<ILeaseDetails>;
type EntityArrayResponseType = HttpResponse<ILeaseDetails[]>;

@Injectable({ providedIn: 'root' })
export class LeaseDetailsService {
  public resourceUrl = SERVER_API_URL + 'api/lease-details';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/lease-details';

  constructor(protected http: HttpClient) {}

  create(leaseDetails: ILeaseDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseDetails);
    return this.http
      .post<ILeaseDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseDetails: ILeaseDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseDetails);
    return this.http
      .put<ILeaseDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(leaseDetails: ILeaseDetails): ILeaseDetails {
    const copy: ILeaseDetails = Object.assign({}, leaseDetails, {
      commencementDate:
        leaseDetails.commencementDate && leaseDetails.commencementDate.isValid()
          ? leaseDetails.commencementDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commencementDate = res.body.commencementDate ? moment(res.body.commencementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaseDetails: ILeaseDetails) => {
        leaseDetails.commencementDate = leaseDetails.commencementDate ? moment(leaseDetails.commencementDate) : undefined;
      });
    }
    return res;
  }
}
