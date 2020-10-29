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

import { SpyObject } from './spyobject';
import { StateStorageService } from 'app/core/auth/state-storage.service';

export class MockStateStorageService extends SpyObject {
  getUrlSpy: Spy;
  storeUrlSpy: Spy;
  clearUrlSpy: Spy;

  constructor() {
    super(StateStorageService);
    this.getUrlSpy = this.spy('getUrl').andReturn(null);
    this.storeUrlSpy = this.spy('storeUrl').andReturn(this);
    this.clearUrlSpy = this.spy('clearUrl').andReturn(this);
  }

  setResponse(previousUrl: string | null): void {
    this.getUrlSpy = this.spy('getUrl').andReturn(previousUrl);
  }
}
