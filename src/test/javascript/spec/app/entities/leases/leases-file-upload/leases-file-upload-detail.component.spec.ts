import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesFileUploadDetailComponent } from 'app/entities/leases/leases-file-upload/leases-file-upload-detail.component';
import { LeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';

describe('Component Tests', () => {
  describe('LeasesFileUpload Management Detail Component', () => {
    let comp: LeasesFileUploadDetailComponent;
    let fixture: ComponentFixture<LeasesFileUploadDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ leasesFileUpload: new LeasesFileUpload(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesFileUploadDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LeasesFileUploadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeasesFileUploadDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load leasesFileUpload on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leasesFileUpload).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
