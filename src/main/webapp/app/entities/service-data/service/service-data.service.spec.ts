import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IServiceData, ServiceData } from '../service-data.model';

import { ServiceDataService } from './service-data.service';

describe('ServiceData Service', () => {
  let service: ServiceDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IServiceData;
  let expectedResult: IServiceData | IServiceData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceDataService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      lang: Language.FR,
      title: 'AAAAAAA',
      content: 'AAAAAAA',
      imageContentType: 'image/png',
      image: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ServiceData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ServiceData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lang: 'BBBBBB',
          title: 'BBBBBB',
          content: 'BBBBBB',
          image: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          title: 'BBBBBB',
          content: 'BBBBBB',
          image: 'BBBBBB',
        },
        new ServiceData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceData', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lang: 'BBBBBB',
          title: 'BBBBBB',
          content: 'BBBBBB',
          image: 'BBBBBB',
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

    it('should delete a ServiceData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addServiceDataToCollectionIfMissing', () => {
      it('should add a ServiceData to an empty array', () => {
        const serviceData: IServiceData = { id: 123 };
        expectedResult = service.addServiceDataToCollectionIfMissing([], serviceData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceData);
      });

      it('should not add a ServiceData to an array that contains it', () => {
        const serviceData: IServiceData = { id: 123 };
        const serviceDataCollection: IServiceData[] = [
          {
            ...serviceData,
          },
          { id: 456 },
        ];
        expectedResult = service.addServiceDataToCollectionIfMissing(serviceDataCollection, serviceData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceData to an array that doesn't contain it", () => {
        const serviceData: IServiceData = { id: 123 };
        const serviceDataCollection: IServiceData[] = [{ id: 456 }];
        expectedResult = service.addServiceDataToCollectionIfMissing(serviceDataCollection, serviceData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceData);
      });

      it('should add only unique ServiceData to an array', () => {
        const serviceDataArray: IServiceData[] = [{ id: 123 }, { id: 456 }, { id: 63859 }];
        const serviceDataCollection: IServiceData[] = [{ id: 123 }];
        expectedResult = service.addServiceDataToCollectionIfMissing(serviceDataCollection, ...serviceDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceData: IServiceData = { id: 123 };
        const serviceData2: IServiceData = { id: 456 };
        expectedResult = service.addServiceDataToCollectionIfMissing([], serviceData, serviceData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceData);
        expect(expectedResult).toContain(serviceData2);
      });

      it('should accept null and undefined values', () => {
        const serviceData: IServiceData = { id: 123 };
        expectedResult = service.addServiceDataToCollectionIfMissing([], null, serviceData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceData);
      });

      it('should return initial array if no ServiceData is added', () => {
        const serviceDataCollection: IServiceData[] = [{ id: 123 }];
        expectedResult = service.addServiceDataToCollectionIfMissing(serviceDataCollection, undefined, null);
        expect(expectedResult).toEqual(serviceDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
