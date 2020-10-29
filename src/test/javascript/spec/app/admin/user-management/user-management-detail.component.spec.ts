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
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Authority } from 'app/shared/constants/authority.constants';
import { LeasesTestModule } from '../../../test.module';
import { UserManagementDetailComponent } from 'app/admin/user-management/user-management-detail.component';
import { User } from 'app/core/user/user.model';

describe('Component Tests', () => {
  describe('User Management Detail Component', () => {
    let comp: UserManagementDetailComponent;
    let fixture: ComponentFixture<UserManagementDetailComponent>;
    const route: ActivatedRoute = ({
      data: of({ user: new User(1, 'user', 'first', 'last', 'first@last.com', true, 'en', [Authority.USER], 'admin') }),
    } as any) as ActivatedRoute;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [UserManagementDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: route,
          },
        ],
      })
        .overrideTemplate(UserManagementDetailComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserManagementDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.user).toEqual(
          jasmine.objectContaining({
            id: 1,
            login: 'user',
            firstName: 'first',
            lastName: 'last',
            email: 'first@last.com',
            activated: true,
            langKey: 'en',
            authorities: [Authority.USER],
            createdBy: 'admin',
          })
        );
      });
    });
  });
});
