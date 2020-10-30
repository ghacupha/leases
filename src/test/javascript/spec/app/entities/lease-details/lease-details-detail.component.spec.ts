import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../test.module';
import { LeaseDetailsDetailComponent } from 'app/entities/lease-details/lease-details-detail.component';
import { LeaseDetails } from 'app/shared/model/lease-details.model';

describe('Component Tests', () => {
  describe('LeaseDetails Management Detail Component', () => {
    let comp: LeaseDetailsDetailComponent;
    let fixture: ComponentFixture<LeaseDetailsDetailComponent>;
    const route = ({ data: of({ leaseDetails: new LeaseDetails(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeaseDetailsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LeaseDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeaseDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leaseDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leaseDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
