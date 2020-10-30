import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ILeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';

type EntityResponseType = HttpResponse<ILeasesFileUpload>;
type EntityArrayResponseType = HttpResponse<ILeasesFileUpload[]>;

@Injectable({ providedIn: 'root' })
export class LeasesFileUploadService {
  public resourceUrl = SERVER_API_URL + 'api/app/file-uploads';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/leases-file-uploads';

  constructor(protected http: HttpClient) {}

  create(leasesFileUpload: ILeasesFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasesFileUpload);
    return this.http
      .post<ILeasesFileUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leasesFileUpload: ILeasesFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasesFileUpload);
    return this.http
      .put<ILeasesFileUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeasesFileUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasesFileUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasesFileUpload[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(leasesFileUpload: ILeasesFileUpload): ILeasesFileUpload {
    const copy: ILeasesFileUpload = Object.assign({}, leasesFileUpload, {
      periodFrom:
        leasesFileUpload.periodFrom && leasesFileUpload.periodFrom.isValid() ? leasesFileUpload.periodFrom.format(DATE_FORMAT) : undefined,
      periodTo:
        leasesFileUpload.periodTo && leasesFileUpload.periodTo.isValid() ? leasesFileUpload.periodTo.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.periodFrom = res.body.periodFrom ? moment(res.body.periodFrom) : undefined;
      res.body.periodTo = res.body.periodTo ? moment(res.body.periodTo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leasesFileUpload: ILeasesFileUpload) => {
        leasesFileUpload.periodFrom = leasesFileUpload.periodFrom ? moment(leasesFileUpload.periodFrom) : undefined;
        leasesFileUpload.periodTo = leasesFileUpload.periodTo ? moment(leasesFileUpload.periodTo) : undefined;
      });
    }
    return res;
  }
}
