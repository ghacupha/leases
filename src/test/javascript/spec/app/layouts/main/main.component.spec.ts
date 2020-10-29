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

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, RouterEvent, NavigationEnd } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Subject } from 'rxjs';

import { MainComponent } from 'app/layouts/main/main.component';
import { LeasesTestModule } from '../../../test.module';
import { MockRouter } from '../../../helpers/mock-route.service';

describe('Component Tests', () => {
  describe('MainComponent', () => {
    let comp: MainComponent;
    let fixture: ComponentFixture<MainComponent>;
    let router: MockRouter;
    const routerEventsSubject = new Subject<RouterEvent>();
    let titleService: Title;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [MainComponent],
        providers: [Title],
      })
        .overrideTemplate(MainComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(MainComponent);
      comp = fixture.componentInstance;
      router = TestBed.get(Router);
      router.setEvents(routerEventsSubject.asObservable());
      titleService = TestBed.get(Title);
    });

    describe('page title', () => {
      let routerState: any;
      const defaultPageTitle = 'Leases';
      const parentRoutePageTitle = 'parentTitle';
      const childRoutePageTitle = 'childTitle';
      const navigationEnd = new NavigationEnd(1, '', '');

      beforeEach(() => {
        routerState = { snapshot: { root: {} } };
        router.setRouterState(routerState);
        spyOn(titleService, 'setTitle');
        comp.ngOnInit();
      });

      describe('navigation end', () => {
        it('should set page title to default title if pageTitle is missing on routes', () => {
          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(defaultPageTitle);
        });

        it('should set page title to root route pageTitle if there is no child routes', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(parentRoutePageTitle);
        });

        it('should set page title to child route pageTitle if child routes exist and pageTitle is set for child route', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };
          routerState.snapshot.root.firstChild = { data: { pageTitle: childRoutePageTitle } };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(childRoutePageTitle);
        });

        it('should set page title to parent route pageTitle if child routes exists but pageTitle is not set for child route data', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };
          routerState.snapshot.root.firstChild = { data: {} };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(parentRoutePageTitle);
        });

        it('should set page title to parent route pageTitle if child routes exists but data is not set for child route', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };
          routerState.snapshot.root.firstChild = {};

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(parentRoutePageTitle);
        });
      });
    });
  });
});
