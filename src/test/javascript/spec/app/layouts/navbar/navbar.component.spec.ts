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
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { ProfileInfo } from 'app/layouts/profiles/profile-info.model';
import { NavbarComponent } from 'app/layouts/navbar/navbar.component';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';

describe('Component Tests', () => {
  describe('Navbar Component', () => {
    let comp: NavbarComponent;
    let fixture: ComponentFixture<NavbarComponent>;
    let accountService: AccountService;
    let profileService: ProfileService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [NavbarComponent],
      })
        .overrideTemplate(NavbarComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(NavbarComponent);
      comp = fixture.componentInstance;
      accountService = TestBed.get(AccountService);
      profileService = TestBed.get(ProfileService);
    });

    it('Should call profileService.getProfileInfo on init', () => {
      // GIVEN
      spyOn(profileService, 'getProfileInfo').and.returnValue(of(new ProfileInfo()));

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(profileService.getProfileInfo).toHaveBeenCalled();
    });

    it('Should call accountService.isAuthenticated on authentication', () => {
      // WHEN
      comp.isAuthenticated();

      // THEN
      expect(accountService.isAuthenticated).toHaveBeenCalled();
    });
  });
});
