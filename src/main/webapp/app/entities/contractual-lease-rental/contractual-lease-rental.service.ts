import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';

type EntityResponseType = HttpResponse<IContractualLeaseRental>;
type EntityArrayResponseType = HttpResponse<IContractualLeaseRental[]>;

@Injectable({ providedIn: 'root' })
export class ContractualLeaseRentalService {
  public resourceUrl = SERVER_API_URL + 'api/contractual-lease-rentals';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/contractual-lease-rentals';

  constructor(protected http: HttpClient) {}

  create(contractualLeaseRental: IContractualLeaseRental): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractualLeaseRental);
    return this.http
      .post<IContractualLeaseRental>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contractualLeaseRental: IContractualLeaseRental): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractualLeaseRental);
    return this.http
      .put<IContractualLeaseRental>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContractualLeaseRental>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContractualLeaseRental[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContractualLeaseRental[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(contractualLeaseRental: IContractualLeaseRental): IContractualLeaseRental {
    const copy: IContractualLeaseRental = Object.assign({}, contractualLeaseRental, {
      leaseRentalDate:
        contractualLeaseRental.leaseRentalDate && contractualLeaseRental.leaseRentalDate.isValid()
          ? contractualLeaseRental.leaseRentalDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.leaseRentalDate = res.body.leaseRentalDate ? moment(res.body.leaseRentalDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contractualLeaseRental: IContractualLeaseRental) => {
        contractualLeaseRental.leaseRentalDate = contractualLeaseRental.leaseRentalDate
          ? moment(contractualLeaseRental.leaseRentalDate)
          : undefined;
      });
    }
    return res;
  }
}
