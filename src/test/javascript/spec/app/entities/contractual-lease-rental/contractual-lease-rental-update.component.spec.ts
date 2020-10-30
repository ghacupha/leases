import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { ContractualLeaseRentalUpdateComponent } from 'app/entities/contractual-lease-rental/contractual-lease-rental-update.component';
import { ContractualLeaseRentalService } from 'app/entities/contractual-lease-rental/contractual-lease-rental.service';
import { ContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';

describe('Component Tests', () => {
  describe('ContractualLeaseRental Management Update Component', () => {
    let comp: ContractualLeaseRentalUpdateComponent;
    let fixture: ComponentFixture<ContractualLeaseRentalUpdateComponent>;
    let service: ContractualLeaseRentalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [ContractualLeaseRentalUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ContractualLeaseRentalUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContractualLeaseRentalUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContractualLeaseRentalService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ContractualLeaseRental(123);
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
        const entity = new ContractualLeaseRental();
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
