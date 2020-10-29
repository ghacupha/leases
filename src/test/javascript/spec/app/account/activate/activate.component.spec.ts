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

import { TestBed, async, tick, fakeAsync, inject } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ActivateService } from 'app/account/activate/activate.service';
import { ActivateComponent } from 'app/account/activate/activate.component';

describe('Component Tests', () => {
  describe('ActivateComponent', () => {
    let comp: ActivateComponent;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [ActivateComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: new MockActivatedRoute({ key: 'ABC123' }),
          },
        ],
      })
        .overrideTemplate(ActivateComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      const fixture = TestBed.createComponent(ActivateComponent);
      comp = fixture.componentInstance;
    });

    it('calls activate.get with the key from params', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(of());

        comp.ngOnInit();
        tick();

        expect(service.get).toHaveBeenCalledWith('ABC123');
      })
    ));

    it('should set set success to true upon successful activation', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(of({}));

        comp.ngOnInit();
        tick();

        expect(comp.error).toBe(false);
        expect(comp.success).toBe(true);
      })
    ));

    it('should set set error to true upon activation failure', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(throwError('ERROR'));

        comp.ngOnInit();
        tick();

        expect(comp.error).toBe(true);
        expect(comp.success).toBe(false);
      })
    ));
  });
});
