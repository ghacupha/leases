import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesMessageTokenComponent } from 'app/entities/leases/leases-message-token/leases-message-token.component';
import { LeasesMessageTokenService } from 'app/entities/leases/leases-message-token/leases-message-token.service';
import { LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

describe('Component Tests', () => {
  describe('LeasesMessageToken Management Component', () => {
    let comp: LeasesMessageTokenComponent;
    let fixture: ComponentFixture<LeasesMessageTokenComponent>;
    let service: LeasesMessageTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesMessageTokenComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(LeasesMessageTokenComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeasesMessageTokenComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeasesMessageTokenService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeasesMessageToken(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leasesMessageTokens && comp.leasesMessageTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeasesMessageToken(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leasesMessageTokens && comp.leasesMessageTokens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
