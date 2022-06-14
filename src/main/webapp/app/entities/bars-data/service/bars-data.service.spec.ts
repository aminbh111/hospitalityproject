import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IBarsData, BarsData } from '../bars-data.model';

import { BarsDataService } from './bars-data.service';

describe('BarsData Service', () => {
  let service: BarsDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IBarsData;
  let expectedResult: IBarsData | IBarsData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BarsDataService);
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

    it('should create a BarsData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BarsData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BarsData', () => {
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

    it('should partial update a BarsData', () => {
      const patchObject = Object.assign(
        {
          image: 'BBBBBB',
        },
        new BarsData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BarsData', () => {
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

    it('should delete a BarsData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBarsDataToCollectionIfMissing', () => {
      it('should add a BarsData to an empty array', () => {
        const barsData: IBarsData = { id: 123 };
        expectedResult = service.addBarsDataToCollectionIfMissing([], barsData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(barsData);
      });

      it('should not add a BarsData to an array that contains it', () => {
        const barsData: IBarsData = { id: 123 };
        const barsDataCollection: IBarsData[] = [
          {
            ...barsData,
          },
          { id: 456 },
        ];
        expectedResult = service.addBarsDataToCollectionIfMissing(barsDataCollection, barsData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BarsData to an array that doesn't contain it", () => {
        const barsData: IBarsData = { id: 123 };
        const barsDataCollection: IBarsData[] = [{ id: 456 }];
        expectedResult = service.addBarsDataToCollectionIfMissing(barsDataCollection, barsData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(barsData);
      });

      it('should add only unique BarsData to an array', () => {
        const barsDataArray: IBarsData[] = [{ id: 123 }, { id: 456 }, { id: 26169 }];
        const barsDataCollection: IBarsData[] = [{ id: 123 }];
        expectedResult = service.addBarsDataToCollectionIfMissing(barsDataCollection, ...barsDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const barsData: IBarsData = { id: 123 };
        const barsData2: IBarsData = { id: 456 };
        expectedResult = service.addBarsDataToCollectionIfMissing([], barsData, barsData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(barsData);
        expect(expectedResult).toContain(barsData2);
      });

      it('should accept null and undefined values', () => {
        const barsData: IBarsData = { id: 123 };
        expectedResult = service.addBarsDataToCollectionIfMissing([], null, barsData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(barsData);
      });

      it('should return initial array if no BarsData is added', () => {
        const barsDataCollection: IBarsData[] = [{ id: 123 }];
        expectedResult = service.addBarsDataToCollectionIfMissing(barsDataCollection, undefined, null);
        expect(expectedResult).toEqual(barsDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
