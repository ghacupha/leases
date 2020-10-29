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
import { LogsComponent } from 'app/admin/logs/logs.component';
import { LogsService } from 'app/admin/logs/logs.service';
import { Log } from 'app/admin/logs/log.model';

describe('Component Tests', () => {
  describe('LogsComponent', () => {
    let comp: LogsComponent;
    let fixture: ComponentFixture<LogsComponent>;
    let service: LogsService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LogsComponent],
        providers: [LogsService],
      })
        .overrideTemplate(LogsComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(LogsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LogsService);
    });

    describe('OnInit', () => {
      it('should set all default values correctly', () => {
        expect(comp.filter).toBe('');
        expect(comp.orderProp).toBe('name');
        expect(comp.reverse).toBe(false);
      });

      it('Should call load all on init', () => {
        // GIVEN
        const log = new Log('main', 'WARN');
        spyOn(service, 'findAll').and.returnValue(
          of({
            loggers: {
              main: {
                effectiveLevel: 'WARN',
              },
            },
          })
        );

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.findAll).toHaveBeenCalled();
        expect(comp.loggers && comp.loggers[0]).toEqual(jasmine.objectContaining(log));
      });
    });

    describe('change log level', () => {
      it('should change log level correctly', () => {
        // GIVEN
        const log = new Log('main', 'ERROR');
        spyOn(service, 'changeLevel').and.returnValue(of({}));
        spyOn(service, 'findAll').and.returnValue(
          of({
            loggers: {
              main: {
                effectiveLevel: 'ERROR',
              },
            },
          })
        );

        // WHEN
        comp.changeLevel('main', 'ERROR');

        // THEN
        expect(service.changeLevel).toHaveBeenCalled();
        expect(service.findAll).toHaveBeenCalled();
        expect(comp.loggers && comp.loggers[0]).toEqual(jasmine.objectContaining(log));
      });
    });
  });
});
