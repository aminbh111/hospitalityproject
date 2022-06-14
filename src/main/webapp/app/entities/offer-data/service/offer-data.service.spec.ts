import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IOfferData, OfferData } from '../offer-data.model';

import { OfferDataService } from './offer-data.service';

describe('OfferData Service', () => {
  let service: OfferDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IOfferData;
  let expectedResult: IOfferData | IOfferData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OfferDataService);
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

    it('should create a OfferData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OfferData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OfferData', () => {
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

    it('should partial update a OfferData', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          image: 'BBBBBB',
        },
        new OfferData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OfferData', () => {
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

    it('should delete a OfferData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOfferDataToCollectionIfMissing', () => {
      it('should add a OfferData to an empty array', () => {
        const offerData: IOfferData = { id: 123 };
        expectedResult = service.addOfferDataToCollectionIfMissing([], offerData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offerData);
      });

      it('should not add a OfferData to an array that contains it', () => {
        const offerData: IOfferData = { id: 123 };
        const offerDataCollection: IOfferData[] = [
          {
            ...offerData,
          },
          { id: 456 },
        ];
        expectedResult = service.addOfferDataToCollectionIfMissing(offerDataCollection, offerData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OfferData to an array that doesn't contain it", () => {
        const offerData: IOfferData = { id: 123 };
        const offerDataCollection: IOfferData[] = [{ id: 456 }];
        expectedResult = service.addOfferDataToCollectionIfMissing(offerDataCollection, offerData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offerData);
      });

      it('should add only unique OfferData to an array', () => {
        const offerDataArray: IOfferData[] = [{ id: 123 }, { id: 456 }, { id: 97629 }];
        const offerDataCollection: IOfferData[] = [{ id: 123 }];
        expectedResult = service.addOfferDataToCollectionIfMissing(offerDataCollection, ...offerDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const offerData: IOfferData = { id: 123 };
        const offerData2: IOfferData = { id: 456 };
        expectedResult = service.addOfferDataToCollectionIfMissing([], offerData, offerData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(offerData);
        expect(expectedResult).toContain(offerData2);
      });

      it('should accept null and undefined values', () => {
        const offerData: IOfferData = { id: 123 };
        expectedResult = service.addOfferDataToCollectionIfMissing([], null, offerData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(offerData);
      });

      it('should return initial array if no OfferData is added', () => {
        const offerDataCollection: IOfferData[] = [{ id: 123 }];
        expectedResult = service.addOfferDataToCollectionIfMissing(offerDataCollection, undefined, null);
        expect(expectedResult).toEqual(offerDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
