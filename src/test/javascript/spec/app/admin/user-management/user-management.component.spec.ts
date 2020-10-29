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
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { UserManagementComponent } from 'app/admin/user-management/user-management.component';
import { UserService } from 'app/core/user/user.service';
import { User } from 'app/core/user/user.model';

describe('Component Tests', () => {
  describe('User Management Component', () => {
    let comp: UserManagementComponent;
    let fixture: ComponentFixture<UserManagementComponent>;
    let service: UserService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [UserManagementComponent],
      })
        .overrideTemplate(UserManagementComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(UserManagementComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const headers = new HttpHeaders().append('link', 'link;link');
          spyOn(service, 'query').and.returnValue(
            of(
              new HttpResponse({
                body: [new User(123)],
                headers,
              })
            )
          );

          // WHEN
          comp.ngOnInit();
          tick(); // simulate async

          // THEN
          expect(service.query).toHaveBeenCalled();
          expect(comp.users && comp.users[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        })
      ));
    });

    describe('setActive', () => {
      it('Should update user and call load all', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          const headers = new HttpHeaders().append('link', 'link;link');
          const user = new User(123);
          spyOn(service, 'query').and.returnValue(
            of(
              new HttpResponse({
                body: [user],
                headers,
              })
            )
          );
          spyOn(service, 'update').and.returnValue(of(new HttpResponse({ status: 200 })));

          // WHEN
          comp.setActive(user, true);
          tick(); // simulate async

          // THEN
          expect(service.update).toHaveBeenCalledWith({ ...user, activated: true });
          expect(service.query).toHaveBeenCalled();
          expect(comp.users && comp.users[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        })
      ));
    });
  });
});
