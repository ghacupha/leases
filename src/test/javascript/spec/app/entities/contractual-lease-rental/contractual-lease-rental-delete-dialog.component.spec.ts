import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LeasesTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ContractualLeaseRentalDeleteDialogComponent } from 'app/entities/contractual-lease-rental/contractual-lease-rental-delete-dialog.component';
import { ContractualLeaseRentalService } from 'app/entities/contractual-lease-rental/contractual-lease-rental.service';

describe('Component Tests', () => {
  describe('ContractualLeaseRental Management Delete Component', () => {
    let comp: ContractualLeaseRentalDeleteDialogComponent;
    let fixture: ComponentFixture<ContractualLeaseRentalDeleteDialogComponent>;
    let service: ContractualLeaseRentalService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [ContractualLeaseRentalDeleteDialogComponent],
      })
        .overrideTemplate(ContractualLeaseRentalDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContractualLeaseRentalDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContractualLeaseRentalService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
