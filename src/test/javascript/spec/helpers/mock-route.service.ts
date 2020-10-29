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

import Spy = jasmine.Spy;
import { ActivatedRoute, Router, RouterEvent, Data, Params } from '@angular/router';
import { Observable, ReplaySubject } from 'rxjs';

import { SpyObject } from './spyobject';

export class MockActivatedRoute extends ActivatedRoute {
  private queryParamsSubject = new ReplaySubject<Params>();
  private paramSubject = new ReplaySubject<Params>();
  private dataSubject = new ReplaySubject<Data>();

  constructor(parameters: Params) {
    super();
    this.queryParams = this.queryParamsSubject.asObservable();
    this.params = this.paramSubject.asObservable();
    this.data = this.dataSubject.asObservable();
    this.setParameters(parameters);
  }

  setParameters(parameters: Params): void {
    this.queryParamsSubject.next(parameters);
    this.paramSubject.next(parameters);
    this.dataSubject.next({
      ...parameters,
      defaultSort: 'id,desc',
    });
  }
}

export class MockRouter extends SpyObject {
  navigateSpy: Spy;
  navigateByUrlSpy: Spy;
  events: Observable<RouterEvent> | null = null;
  routerState: any;
  url = '';

  constructor() {
    super(Router);
    this.navigateSpy = this.spy('navigate');
    this.navigateByUrlSpy = this.spy('navigateByUrl');
  }

  setEvents(events: Observable<RouterEvent>): void {
    this.events = events;
  }

  setRouterState(routerState: any): void {
    this.routerState = routerState;
  }
}
