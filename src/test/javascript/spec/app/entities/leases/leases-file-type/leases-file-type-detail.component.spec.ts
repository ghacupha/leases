import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesFileTypeDetailComponent } from 'app/entities/leases/leases-file-type/leases-file-type-detail.component';
import { LeasesFileType } from 'app/shared/model/leases/leases-file-type.model';

describe('Component Tests', () => {
  describe('LeasesFileType Management Detail Component', () => {
    let comp: LeasesFileTypeDetailComponent;
    let fixture: ComponentFixture<LeasesFileTypeDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ leasesFileType: new LeasesFileType(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesFileTypeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LeasesFileTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeasesFileTypeDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load leasesFileType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leasesFileType).toEqual(jasmine.objectContaining({ id: 123 }));
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
