import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { LeaseDetailsService } from 'app/entities/lease-details/lease-details.service';
import { ILeaseDetails, LeaseDetails } from 'app/shared/model/lease-details.model';

describe('Service Tests', () => {
  describe('LeaseDetails Service', () => {
    let injector: TestBed;
    let service: LeaseDetailsService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeaseDetails;
    let expectedResult: ILeaseDetails | ILeaseDetails[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LeaseDetailsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new LeaseDetails(0, 'AAAAAAA', 0, currentDate, 0, 0, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            commencementDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeaseDetails', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            commencementDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            commencementDate: currentDate,
          },
          returnedFromService
        );

        service.create(new LeaseDetails()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeaseDetails', () => {
        const returnedFromService = Object.assign(
          {
            leaseContractNumber: 'BBBBBB',
            incrementalBorrowingRate: 1,
            commencementDate: currentDate.format(DATE_FORMAT),
            leasePrepayments: 1,
            initialDirectCosts: 1,
            demolitionCosts: 1,
            assetAccountNumber: 'BBBBBB',
            liabilityAccountNumber: 'BBBBBB',
            depreciationAccountNumber: 'BBBBBB',
            interestAccountNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            commencementDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeaseDetails', () => {
        const returnedFromService = Object.assign(
          {
            leaseContractNumber: 'BBBBBB',
            incrementalBorrowingRate: 1,
            commencementDate: currentDate.format(DATE_FORMAT),
            leasePrepayments: 1,
            initialDirectCosts: 1,
            demolitionCosts: 1,
            assetAccountNumber: 'BBBBBB',
            liabilityAccountNumber: 'BBBBBB',
            depreciationAccountNumber: 'BBBBBB',
            interestAccountNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            commencementDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LeaseDetails', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
