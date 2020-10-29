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
import { FormBuilder } from '@angular/forms';
import { throwError, of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { SettingsComponent } from 'app/account/settings/settings.component';
import { MockAccountService } from '../../../helpers/mock-account.service';

describe('Component Tests', () => {
  describe('SettingsComponent', () => {
    let comp: SettingsComponent;
    let fixture: ComponentFixture<SettingsComponent>;
    let mockAuth: MockAccountService;
    const accountValues: Account = {
      firstName: 'John',
      lastName: 'Doe',
      activated: true,
      email: 'john.doe@mail.com',
      langKey: 'en',
      login: 'john',
      authorities: [],
      imageUrl: '',
    };

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [SettingsComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SettingsComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(SettingsComponent);
      comp = fixture.componentInstance;
      mockAuth = TestBed.get(AccountService);
      mockAuth.setIdentityResponse(accountValues);
    });

    it('should send the current identity upon save', () => {
      // GIVEN
      mockAuth.saveSpy.and.returnValue(of({}));
      const settingsFormValues = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@mail.com',
      };

      // WHEN
      comp.ngOnInit();
      comp.save();

      // THEN
      expect(mockAuth.identitySpy).toHaveBeenCalled();
      expect(mockAuth.saveSpy).toHaveBeenCalledWith(accountValues);
      expect(mockAuth.authenticateSpy).toHaveBeenCalledWith(accountValues);
      expect(comp.settingsForm.value).toEqual(settingsFormValues);
    });

    it('should notify of success upon successful save', () => {
      // GIVEN
      mockAuth.saveSpy.and.returnValue(of({}));

      // WHEN
      comp.ngOnInit();
      comp.save();

      // THEN
      expect(comp.success).toBe(true);
    });

    it('should notify of error upon failed save', () => {
      // GIVEN
      mockAuth.saveSpy.and.returnValue(throwError('ERROR'));

      // WHEN
      comp.ngOnInit();
      comp.save();

      // THEN
      expect(comp.success).toBe(false);
    });
  });
});
