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
import { of } from 'rxjs';

import { SpyObject } from './spyobject';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

export class MockAccountService extends SpyObject {
  getSpy: Spy;
  saveSpy: Spy;
  authenticateSpy: Spy;
  identitySpy: Spy;
  getAuthenticationStateSpy: Spy;
  isAuthenticated: Spy;

  constructor() {
    super(AccountService);

    this.getSpy = this.spy('get').andReturn(this);
    this.saveSpy = this.spy('save').andReturn(this);
    this.authenticateSpy = this.spy('authenticate').andReturn(this);
    this.identitySpy = this.spy('identity').andReturn(of(null));
    this.getAuthenticationStateSpy = this.spy('getAuthenticationState').andReturn(of(null));
    this.isAuthenticated = this.spy('isAuthenticated').andReturn(true);
  }

  setIdentityResponse(account: Account | null): void {
    this.identitySpy = this.spy('identity').andReturn(of(account));
    this.getAuthenticationStateSpy = this.spy('getAuthenticationState').andReturn(of(account));
  }
}
