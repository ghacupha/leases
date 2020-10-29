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

import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { PasswordComponent } from 'app/account/password/password.component';
import { PasswordService } from 'app/account/password/password.service';

describe('Component Tests', () => {
  describe('PasswordComponent', () => {
    let comp: PasswordComponent;
    let fixture: ComponentFixture<PasswordComponent>;
    let service: PasswordService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [PasswordComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PasswordComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(PasswordComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PasswordService);
    });

    it('should show error if passwords do not match', () => {
      // GIVEN
      comp.passwordForm.patchValue({
        newPassword: 'password1',
        confirmPassword: 'password2',
      });
      // WHEN
      comp.changePassword();
      // THEN
      expect(comp.doNotMatch).toBe(true);
      expect(comp.error).toBe(false);
      expect(comp.success).toBe(false);
    });

    it('should call Auth.changePassword when passwords match', () => {
      // GIVEN
      const passwordValues = {
        currentPassword: 'oldPassword',
        newPassword: 'myPassword',
      };

      spyOn(service, 'save').and.returnValue(of(new HttpResponse({ body: true })));

      comp.passwordForm.patchValue({
        currentPassword: passwordValues.currentPassword,
        newPassword: passwordValues.newPassword,
        confirmPassword: passwordValues.newPassword,
      });

      // WHEN
      comp.changePassword();

      // THEN
      expect(service.save).toHaveBeenCalledWith(passwordValues.newPassword, passwordValues.currentPassword);
    });

    it('should set success to true upon success', () => {
      // GIVEN
      spyOn(service, 'save').and.returnValue(of(new HttpResponse({ body: true })));
      comp.passwordForm.patchValue({
        newPassword: 'myPassword',
        confirmPassword: 'myPassword',
      });

      // WHEN
      comp.changePassword();

      // THEN
      expect(comp.doNotMatch).toBe(false);
      expect(comp.error).toBe(false);
      expect(comp.success).toBe(true);
    });

    it('should notify of error if change password fails', () => {
      // GIVEN
      spyOn(service, 'save').and.returnValue(throwError('ERROR'));
      comp.passwordForm.patchValue({
        newPassword: 'myPassword',
        confirmPassword: 'myPassword',
      });

      // WHEN
      comp.changePassword();

      // THEN
      expect(comp.doNotMatch).toBe(false);
      expect(comp.success).toBe(false);
      expect(comp.error).toBe(true);
    });
  });
});
