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
import { HttpErrorResponse } from '@angular/common/http';
import { of, throwError } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { HealthComponent } from 'app/admin/health/health.component';
import { HealthService, Health } from 'app/admin/health/health.service';

describe('Component Tests', () => {
  describe('HealthComponent', () => {
    let comp: HealthComponent;
    let fixture: ComponentFixture<HealthComponent>;
    let service: HealthService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [HealthComponent],
      })
        .overrideTemplate(HealthComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(HealthComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(HealthService);
    });

    describe('getBadgeClass', () => {
      it('should get badge class', () => {
        const upBadgeClass = comp.getBadgeClass('UP');
        const downBadgeClass = comp.getBadgeClass('DOWN');
        expect(upBadgeClass).toEqual('badge-success');
        expect(downBadgeClass).toEqual('badge-danger');
      });
    });

    describe('refresh', () => {
      it('should call refresh on init', () => {
        // GIVEN
        const health: Health = { status: 'UP', components: { mail: { status: 'UP', details: 'mailDetails' } } };
        spyOn(service, 'checkHealth').and.returnValue(of(health));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.checkHealth).toHaveBeenCalled();
        expect(comp.health).toEqual(health);
      });

      it('should handle a 503 on refreshing health data', () => {
        // GIVEN
        const health: Health = { status: 'DOWN', components: { mail: { status: 'DOWN', details: 'mailDetails' } } };
        spyOn(service, 'checkHealth').and.returnValue(throwError(new HttpErrorResponse({ status: 503, error: health })));

        // WHEN
        comp.refresh();

        // THEN
        expect(service.checkHealth).toHaveBeenCalled();
        expect(comp.health).toEqual(health);
      });
    });
  });
});
