import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesMessageTokenUpdateComponent } from 'app/entities/leases/leases-message-token/leases-message-token-update.component';
import { LeasesMessageTokenService } from 'app/entities/leases/leases-message-token/leases-message-token.service';
import { LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

describe('Component Tests', () => {
  describe('LeasesMessageToken Management Update Component', () => {
    let comp: LeasesMessageTokenUpdateComponent;
    let fixture: ComponentFixture<LeasesMessageTokenUpdateComponent>;
    let service: LeasesMessageTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesMessageTokenUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LeasesMessageTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LeasesMessageTokenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LeasesMessageTokenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LeasesMessageToken(123);
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
        const entity = new LeasesMessageToken();
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
