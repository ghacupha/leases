import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { ContractualLeaseRentalDetailComponent } from 'app/entities/contractual-lease-rental/contractual-lease-rental-detail.component';
import { ContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';

describe('Component Tests', () => {
  describe('ContractualLeaseRental Management Detail Component', () => {
    let comp: ContractualLeaseRentalDetailComponent;
    let fixture: ComponentFixture<ContractualLeaseRentalDetailComponent>;
    const route = ({ data: of({ contractualLeaseRental: new ContractualLeaseRental(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [ContractualLeaseRentalDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ContractualLeaseRentalDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContractualLeaseRentalDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contractualLeaseRental on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contractualLeaseRental).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
