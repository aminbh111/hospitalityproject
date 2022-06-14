import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IConciergeData, ConciergeData } from '../concierge-data.model';

import { ConciergeDataService } from './concierge-data.service';

describe('ConciergeData Service', () => {
  let service: ConciergeDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IConciergeData;
  let expectedResult: IConciergeData | IConciergeData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConciergeDataService);
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

    it('should create a ConciergeData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ConciergeData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ConciergeData', () => {
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

    it('should partial update a ConciergeData', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
        },
        new ConciergeData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ConciergeData', () => {
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

    it('should delete a ConciergeData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConciergeDataToCollectionIfMissing', () => {
      it('should add a ConciergeData to an empty array', () => {
        const conciergeData: IConciergeData = { id: 123 };
        expectedResult = service.addConciergeDataToCollectionIfMissing([], conciergeData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conciergeData);
      });

      it('should not add a ConciergeData to an array that contains it', () => {
        const conciergeData: IConciergeData = { id: 123 };
        const conciergeDataCollection: IConciergeData[] = [
          {
            ...conciergeData,
          },
          { id: 456 },
        ];
        expectedResult = service.addConciergeDataToCollectionIfMissing(conciergeDataCollection, conciergeData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ConciergeData to an array that doesn't contain it", () => {
        const conciergeData: IConciergeData = { id: 123 };
        const conciergeDataCollection: IConciergeData[] = [{ id: 456 }];
        expectedResult = service.addConciergeDataToCollectionIfMissing(conciergeDataCollection, conciergeData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conciergeData);
      });

      it('should add only unique ConciergeData to an array', () => {
        const conciergeDataArray: IConciergeData[] = [{ id: 123 }, { id: 456 }, { id: 53951 }];
        const conciergeDataCollection: IConciergeData[] = [{ id: 123 }];
        expectedResult = service.addConciergeDataToCollectionIfMissing(conciergeDataCollection, ...conciergeDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const conciergeData: IConciergeData = { id: 123 };
        const conciergeData2: IConciergeData = { id: 456 };
        expectedResult = service.addConciergeDataToCollectionIfMissing([], conciergeData, conciergeData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(conciergeData);
        expect(expectedResult).toContain(conciergeData2);
      });

      it('should accept null and undefined values', () => {
        const conciergeData: IConciergeData = { id: 123 };
        expectedResult = service.addConciergeDataToCollectionIfMissing([], null, conciergeData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(conciergeData);
      });

      it('should return initial array if no ConciergeData is added', () => {
        const conciergeDataCollection: IConciergeData[] = [{ id: 123 }];
        expectedResult = service.addConciergeDataToCollectionIfMissing(conciergeDataCollection, undefined, null);
        expect(expectedResult).toEqual(conciergeDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
