import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesFileTypeUpdateComponent } from 'app/entities/leases/leases-file-type/leases-file-type-update.component';
import { LeasesFileTypeService } from 'app/entities/leases/leases-file-type/leases-file-type.service';
import { LeasesFileType } from 'app/shared/model/leases/leases-file-type.model';

describe('Component Tests', () => {
  describe('LeasesFileType Management Update Component', () => {
    let comp: LeasesFileTypeUpdateComponent;
    let fixture: ComponentFixture<LeasesFileTypeUpdateComponent>;
    let service: LeasesFileTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesFileTypeUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeasesFileTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeasesFileTypeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeasesFileTypeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeasesFileType(123);
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
        const entity = new LeasesFileType();
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
