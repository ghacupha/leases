import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesFileUploadUpdateComponent } from 'app/entities/leases/leases-file-upload/leases-file-upload-update.component';
import { LeasesFileUploadService } from 'app/entities/leases/leases-file-upload/leases-file-upload.service';
import { LeasesFileUpload } from 'app/shared/model/leases/leases-file-upload.model';

describe('Component Tests', () => {
  describe('LeasesFileUpload Management Update Component', () => {
    let comp: LeasesFileUploadUpdateComponent;
    let fixture: ComponentFixture<LeasesFileUploadUpdateComponent>;
    let service: LeasesFileUploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesFileUploadUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeasesFileUploadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeasesFileUploadUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeasesFileUploadService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeasesFileUpload(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeasesFileUpload();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
