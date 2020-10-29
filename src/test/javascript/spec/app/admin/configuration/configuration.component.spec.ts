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
import { ConfigurationComponent } from 'app/admin/configuration/configuration.component';
import { ConfigurationService, Bean, PropertySource } from 'app/admin/configuration/configuration.service';

describe('Component Tests', () => {
  describe('ConfigurationComponent', () => {
    let comp: ConfigurationComponent;
    let fixture: ComponentFixture<ConfigurationComponent>;
    let service: ConfigurationService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [ConfigurationComponent],
        providers: [ConfigurationService],
      })
        .overrideTemplate(ConfigurationComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ConfigurationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ConfigurationService);
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const beans: Bean[] = [
          {
            prefix: 'jhipster',
            properties: {
              clientApp: {
                name: 'jhipsterApp',
              },
            },
          },
        ];
        const propertySources: PropertySource[] = [
          {
            name: 'server.ports',
            properties: {
              'local.server.port': {
                value: '8080',
              },
            },
          },
        ];
        spyOn(service, 'getBeans').and.returnValue(of(beans));
        spyOn(service, 'getPropertySources').and.returnValue(of(propertySources));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getBeans).toHaveBeenCalled();
        expect(service.getPropertySources).toHaveBeenCalled();
        expect(comp.allBeans).toEqual(beans);
        expect(comp.beans).toEqual(beans);
        expect(comp.propertySources).toEqual(propertySources);
      });
    });
  });
});
