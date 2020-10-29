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

import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LoginService } from 'app/core/login/login.service';
import { LoginModalComponent } from 'app/shared/login/login.component';
import { LeasesTestModule } from '../../../test.module';
import { MockLoginService } from '../../../helpers/mock-login.service';
import { MockRouter } from '../../../helpers/mock-route.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';

describe('Component Tests', () => {
  describe('LoginComponent', () => {
    let comp: LoginModalComponent;
    let fixture: ComponentFixture<LoginModalComponent>;
    let mockLoginService: MockLoginService;
    let mockRouter: MockRouter;
    let mockActiveModal: MockActiveModal;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LoginModalComponent],
        providers: [
          FormBuilder,
          {
            provide: LoginService,
            useClass: MockLoginService,
          },
        ],
      })
        .overrideTemplate(LoginModalComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(LoginModalComponent);
      comp = fixture.componentInstance;
      mockLoginService = TestBed.get(LoginService);
      mockRouter = TestBed.get(Router);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    it('should authenticate the user', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        const credentials = {
          username: 'admin',
          password: 'admin',
          rememberMe: true,
        };

        comp.loginForm.patchValue({
          username: 'admin',
          password: 'admin',
          rememberMe: true,
        });
        mockLoginService.setResponse({});
        mockRouter.url = '/admin/metrics';

        // WHEN/
        comp.login();
        tick(); // simulate async

        // THEN
        expect(comp.authenticationError).toEqual(false);
        expect(mockActiveModal.closeSpy).toHaveBeenCalled();
        expect(mockLoginService.loginSpy).toHaveBeenCalledWith(credentials);
      })
    ));

    it('should empty the credentials upon cancel', () => {
      // GIVEN
      comp.loginForm.patchValue({
        username: 'admin',
        password: 'admin',
      });

      const expected = {
        username: '',
        password: '',
        rememberMe: false,
      };

      // WHEN
      comp.cancel();

      // THEN
      expect(comp.authenticationError).toEqual(false);
      expect(comp.loginForm.get('username')!.value).toEqual(expected.username);
      expect(comp.loginForm.get('password')!.value).toEqual(expected.password);
      expect(comp.loginForm.get('rememberMe')!.value).toEqual(expected.rememberMe);
      expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('cancel');
    });

    it('should redirect user when register', () => {
      // WHEN
      comp.register();

      // THEN
      expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('to state register');
      expect(mockRouter.navigateSpy).toHaveBeenCalledWith(['/account/register']);
    });

    it('should redirect user when request password', () => {
      // WHEN
      comp.requestResetPassword();

      // THEN
      expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('to state requestReset');
      expect(mockRouter.navigateSpy).toHaveBeenCalledWith(['/account/reset', 'request']);
    });
  });
});
