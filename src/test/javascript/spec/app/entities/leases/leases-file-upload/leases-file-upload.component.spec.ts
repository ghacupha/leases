import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesFileUploadComponent } from 'app/entities/leases/leases-file-upload/leases-file-upload.component';
import { LeasesFileUploadService } from 'app/entities/leases/leases-file-upload/leases-file-upload.service';
import { LeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';

describe('Component Tests', () => {
  describe('LeasesFileUpload Management Component', () => {
    let comp: LeasesFileUploadComponent;
    let fixture: ComponentFixture<LeasesFileUploadComponent>;
    let service: LeasesFileUploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesFileUploadComponent],
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
        .overrideTemplate(LeasesFileUploadComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeasesFileUploadComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeasesFileUploadService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeasesFileUpload(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leasesFileUploads && comp.leasesFileUploads[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LeasesFileUpload(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.leasesFileUploads && comp.leasesFileUploads[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
