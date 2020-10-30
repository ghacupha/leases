import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LeasesFileTypeService } from 'app/entities/leases/leases-file-type/leases-file-type.service';
import { ILeasesFileType, LeasesFileType } from 'app/shared/model/leases/leases-file-type.model';
import { LeasesFileMediumTypes } from 'app/shared/model/enumerations/leases-file-medium-types.model';
import { LeasesFileModelType } from 'app/shared/model/enumerations/leases-file-model-type.model';

describe('Service Tests', () => {
  describe('LeasesFileType Service', () => {
    let injector: TestBed;
    let service: LeasesFileTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeasesFileType;
    let expectedResult: ILeasesFileType | ILeasesFileType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LeasesFileTypeService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LeasesFileType(
        0,
        'AAAAAAA',
        LeasesFileMediumTypes.EXCEL,
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        LeasesFileModelType.CURRENCY_LIST
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LeasesFileType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LeasesFileType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LeasesFileType', () => {
        const returnedFromService = Object.assign(
          {
            leasesFileTypeName: 'BBBBBB',
            leasesFileMediumType: 'BBBBBB',
            description: 'BBBBBB',
            fileTemplate: 'BBBBBB',
            leasesfileType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LeasesFileType', () => {
        const returnedFromService = Object.assign(
          {
            leasesFileTypeName: 'BBBBBB',
            leasesFileMediumType: 'BBBBBB',
            description: 'BBBBBB',
            fileTemplate: 'BBBBBB',
            leasesfileType: 'BBBBBB',
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

      it('should delete a LeasesFileType', () => {
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
