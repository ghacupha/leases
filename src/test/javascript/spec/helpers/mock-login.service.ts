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
import { LoginService } from 'app/core/login/login.service';

export class MockLoginService extends SpyObject {
  loginSpy: Spy;
  logoutSpy: Spy;
  registerSpy: Spy;
  requestResetPasswordSpy: Spy;
  cancelSpy: Spy;

  constructor() {
    super(LoginService);

    this.loginSpy = this.spy('login').andReturn(of({}));
    this.logoutSpy = this.spy('logout').andReturn(this);
    this.registerSpy = this.spy('register').andReturn(this);
    this.requestResetPasswordSpy = this.spy('requestResetPassword').andReturn(this);
    this.cancelSpy = this.spy('cancel').andReturn(this);
  }

  setResponse(json: any): void {
    this.loginSpy = this.spy('login').andReturn(of(json));
  }
}
