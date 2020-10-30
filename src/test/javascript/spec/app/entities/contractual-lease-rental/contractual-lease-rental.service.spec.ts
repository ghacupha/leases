import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { ContractualLeaseRentalService } from 'app/entities/contractual-lease-rental/contractual-lease-rental.service';
import { IContractualLeaseRental, ContractualLeaseRental } from 'app/shared/model/contractual-lease-rental.model';

describe('Service Tests', () => {
  describe('ContractualLeaseRental Service', () => {
    let injector: TestBed;
    let service: ContractualLeaseRentalService;
    let httpMock: HttpTestingController;
    let elemDefault: IContractualLeaseRental;
    let expectedResult: IContractualLeaseRental | IContractualLeaseRental[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ContractualLeaseRentalService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ContractualLeaseRental(0, 'AAAAAAA', 0, currentDate, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            leaseRentalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContractualLeaseRental', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            leaseRentalDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            leaseRentalDate: currentDate,
          },
          returnedFromService
        );

        service.create(new ContractualLeaseRental()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContractualLeaseRental', () => {
        const returnedFromService = Object.assign(
          {
            leaseContractNumber: 'BBBBBB',
            rentalSequenceNumber: 1,
            leaseRentalDate: currentDate.format(DATE_FORMAT),
            leaseRentalAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            leaseRentalDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContractualLeaseRental', () => {
        const returnedFromService = Object.assign(
          {
            leaseContractNumber: 'BBBBBB',
            rentalSequenceNumber: 1,
            leaseRentalDate: currentDate.format(DATE_FORMAT),
            leaseRentalAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            leaseRentalDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContractualLeaseRental', () => {
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
