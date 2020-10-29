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

import { ElementRef } from '@angular/core';
import { ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';

import { LeasesTestModule } from '../../../../test.module';
import { PasswordResetInitComponent } from 'app/account/password-reset/init/password-reset-init.component';
import { PasswordResetInitService } from 'app/account/password-reset/init/password-reset-init.service';

describe('Component Tests', () => {
  describe('PasswordResetInitComponent', () => {
    let fixture: ComponentFixture<PasswordResetInitComponent>;
    let comp: PasswordResetInitComponent;

    beforeEach(() => {
      fixture = TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [PasswordResetInitComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PasswordResetInitComponent, '')
        .createComponent(PasswordResetInitComponent);
      comp = fixture.componentInstance;
    });

    it('sets focus after the view has been initialized', () => {
      const node = {
        focus(): void {},
      };
      comp.email = new ElementRef(node);
      spyOn(node, 'focus');

      comp.ngAfterViewInit();

      expect(node.focus).toHaveBeenCalled();
    });

    it('notifies of success upon successful requestReset', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(of({}));
      comp.resetRequestForm.patchValue({
        email: 'user@domain.com',
      });

      comp.requestReset();

      expect(service.save).toHaveBeenCalledWith('user@domain.com');
      expect(comp.success).toBe(true);
    }));

    it('no notification of success upon error response', inject([PasswordResetInitService], (service: PasswordResetInitService) => {
      spyOn(service, 'save').and.returnValue(
        throwError({
          status: 503,
          data: 'something else',
        })
      );
      comp.resetRequestForm.patchValue({
        email: 'user@domain.com',
      });
      comp.requestReset();

      expect(service.save).toHaveBeenCalledWith('user@domain.com');
      expect(comp.success).toBe(false);
    }));
  });
});
