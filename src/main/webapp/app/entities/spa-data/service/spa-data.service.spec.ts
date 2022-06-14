import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { ISpaData, SpaData } from '../spa-data.model';

import { SpaDataService } from './spa-data.service';

describe('SpaData Service', () => {
  let service: SpaDataService;
  let httpMock: HttpTestingController;
  let elemDefault: ISpaData;
  let expectedResult: ISpaData | ISpaData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpaDataService);
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

    it('should create a SpaData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SpaData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SpaData', () => {
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

    it('should partial update a SpaData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          title: 'BBBBBB',
          content: 'BBBBBB',
        },
        new SpaData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SpaData', () => {
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

    it('should delete a SpaData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSpaDataToCollectionIfMissing', () => {
      it('should add a SpaData to an empty array', () => {
        const spaData: ISpaData = { id: 123 };
        expectedResult = service.addSpaDataToCollectionIfMissing([], spaData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spaData);
      });

      it('should not add a SpaData to an array that contains it', () => {
        const spaData: ISpaData = { id: 123 };
        const spaDataCollection: ISpaData[] = [
          {
            ...spaData,
          },
          { id: 456 },
        ];
        expectedResult = service.addSpaDataToCollectionIfMissing(spaDataCollection, spaData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SpaData to an array that doesn't contain it", () => {
        const spaData: ISpaData = { id: 123 };
        const spaDataCollection: ISpaData[] = [{ id: 456 }];
        expectedResult = service.addSpaDataToCollectionIfMissing(spaDataCollection, spaData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spaData);
      });

      it('should add only unique SpaData to an array', () => {
        const spaDataArray: ISpaData[] = [{ id: 123 }, { id: 456 }, { id: 33428 }];
        const spaDataCollection: ISpaData[] = [{ id: 123 }];
        expectedResult = service.addSpaDataToCollectionIfMissing(spaDataCollection, ...spaDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const spaData: ISpaData = { id: 123 };
        const spaData2: ISpaData = { id: 456 };
        expectedResult = service.addSpaDataToCollectionIfMissing([], spaData, spaData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spaData);
        expect(expectedResult).toContain(spaData2);
      });

      it('should accept null and undefined values', () => {
        const spaData: ISpaData = { id: 123 };
        expectedResult = service.addSpaDataToCollectionIfMissing([], null, spaData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spaData);
      });

      it('should return initial array if no SpaData is added', () => {
        const spaDataCollection: ISpaData[] = [{ id: 123 }];
        expectedResult = service.addSpaDataToCollectionIfMissing(spaDataCollection, undefined, null);
        expect(expectedResult).toEqual(spaDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
