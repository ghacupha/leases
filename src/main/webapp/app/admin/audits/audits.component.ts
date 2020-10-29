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

import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { combineLatest } from 'rxjs';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { Audit } from './audit.model';
import { AuditsService } from './audits.service';

@Component({
  selector: 'jhi-audit',
  templateUrl: './audits.component.html',
})
export class AuditsComponent implements OnInit {
  audits?: Audit[];
  fromDate = '';
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  toDate = '';
  totalItems = 0;

  private dateFormat = 'yyyy-MM-dd';

  constructor(
    private auditsService: AuditsService,
    private activatedRoute: ActivatedRoute,
    private datePipe: DatePipe,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.toDate = this.today();
    this.fromDate = this.previousMonth();
    this.handleNavigation();
  }

  canLoad(): boolean {
    return this.fromDate !== '' && this.toDate !== '';
  }

  transition(): void {
    if (this.canLoad()) {
      this.router.navigate(['/admin/audits'], {
        queryParams: {
          page: this.page,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          from: this.fromDate,
          to: this.toDate,
        },
      });
    }
  }

  private previousMonth(): string {
    let date = new Date();
    if (date.getMonth() === 0) {
      date = new Date(date.getFullYear() - 1, 11, date.getDate());
    } else {
      date = new Date(date.getFullYear(), date.getMonth() - 1, date.getDate());
    }
    return this.datePipe.transform(date, this.dateFormat)!;
  }

  private today(): string {
    // Today + 1 day - needed if the current day must be included
    const date = new Date();
    date.setDate(date.getDate() + 1);
    return this.datePipe.transform(date, this.dateFormat)!;
  }

  private handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      this.page = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      this.predicate = sort[0];
      this.ascending = sort[1] === 'asc';
      if (params.get('from')) {
        this.fromDate = this.datePipe.transform(params.get('from'), this.dateFormat)!;
      }
      if (params.get('to')) {
        this.toDate = this.datePipe.transform(params.get('to'), this.dateFormat)!;
      }
      this.loadData();
    }).subscribe();
  }

  private loadData(): void {
    this.auditsService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        fromDate: this.fromDate,
        toDate: this.toDate,
      })
      .subscribe((res: HttpResponse<Audit[]>) => this.onSuccess(res.body, res.headers));
  }

  private sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  private onSuccess(audits: Audit[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.audits = audits || [];
  }
}
