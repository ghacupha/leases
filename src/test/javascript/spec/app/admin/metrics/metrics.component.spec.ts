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
import { MetricsComponent } from 'app/admin/metrics/metrics.component';
import { MetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
  describe('MetricsComponent', () => {
    let comp: MetricsComponent;
    let fixture: ComponentFixture<MetricsComponent>;
    let service: MetricsService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [MetricsComponent],
      })
        .overrideTemplate(MetricsComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(MetricsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MetricsService);
    });

    describe('refresh', () => {
      it('should call refresh on init', () => {
        // GIVEN
        const response = {
          timers: {
            service: 'test',
            unrelatedKey: 'test',
          },
          gauges: {
            'jcache.statistics': {
              value: 2,
            },
            unrelatedKey: 'test',
          },
        };
        spyOn(service, 'getMetrics').and.returnValue(of(response));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getMetrics).toHaveBeenCalled();
      });
    });
  });
});
