import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LeasesMessageTokenService } from 'app/entities/leases/leases-message-token/leases-message-token.service';
import { ILeasesMessageToken, LeasesMessageToken } from 'app/shared/model/leases/leases-message-token.model';

describe('Service Tests', () => {
  describe('LeasesMessageToken Service', () => {
    let injector: TestBed;
    let service: LeasesMessageTokenService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeasesMessageToken;
    let expectedResult: ILeasesMessageToken | ILeasesMessageToken[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LeasesMessageTokenService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LeasesMessageToken(0, 'AAAAAAA', 0, 'AAAAAAA', false, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeasesMessageToken', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LeasesMessageToken()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeasesMessageToken', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeasesMessageToken', () => {
        const returnedFromService = Object.assign(
          {
            description: 'BBBBBB',
            timeSent: 1,
            tokenValue: 'BBBBBB',
            received: true,
            actioned: true,
            contentFullyEnqueued: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LeasesMessageToken', () => {
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
