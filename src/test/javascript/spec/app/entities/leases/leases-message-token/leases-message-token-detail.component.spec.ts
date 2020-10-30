import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeasesTestModule } from '../../../../test.module';
import { LeasesMessageTokenDetailComponent } from 'app/entities/leases/leases-message-token/leases-message-token-detail.component';
import { LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

describe('Component Tests', () => {
  describe('LeasesMessageToken Management Detail Component', () => {
    let comp: LeasesMessageTokenDetailComponent;
    let fixture: ComponentFixture<LeasesMessageTokenDetailComponent>;
    const route = ({ data: of({ leasesMessageToken: new LeasesMessageToken(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LeasesTestModule],
        declarations: [LeasesMessageTokenDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LeasesMessageTokenDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LeasesMessageTokenDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load leasesMessageToken on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.leasesMessageToken).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
