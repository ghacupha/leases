import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { LeaseDetailsUpdateComponent } from 'app/entities/lease-details/lease-details-update.component';
import { LeaseDetailsService } from 'app/entities/lease-details/lease-details.service';
import { LeaseDetails } from 'app/shared/model/lease-details.model';

describe('Component Tests', () => {
  describe('LeaseDetails Management Update Component', () => {
    let comp: LeaseDetailsUpdateComponent;
    let fixture: ComponentFixture<LeaseDetailsUpdateComponent>;
    let service: LeaseDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeaseDetailsUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeaseDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeaseDetailsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeaseDetailsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeaseDetails(123);
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
        const entity = new LeaseDetails();
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
