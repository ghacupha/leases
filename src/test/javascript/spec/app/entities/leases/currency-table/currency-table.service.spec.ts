import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CurrencyTableService } from 'app/entities/leases/currency-table/currency-table.service';
import { ICurrencyTable, CurrencyTable } from 'app/shared/model/leases/currency-table.model';
import { CurrencyLocality } from 'app/shared/model/enumerations/currency-locality.model';

describe('Service Tests', () => {
  describe('CurrencyTable Service', () => {
    let injector: TestBed;
    let service: CurrencyTableService;
    let httpMock: HttpTestingController;
    let elemDefault: ICurrencyTable;
    let expectedResult: ICurrencyTable | ICurrencyTable[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CurrencyTableService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new CurrencyTable(0, 'AAAAAAA', CurrencyLocality.LOCAL, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a CurrencyTable', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CurrencyTable()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CurrencyTable', () => {
        const returnedFromService = Object.assign(
          {
            currencyCode: 'BBBBBB',
            locality: 'BBBBBB',
            currencyName: 'BBBBBB',
            country: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CurrencyTable', () => {
        const returnedFromService = Object.assign(
          {
            currencyCode: 'BBBBBB',
            locality: 'BBBBBB',
            currencyName: 'BBBBBB',
            country: 'BBBBBB',
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

      it('should delete a CurrencyTable', () => {
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
