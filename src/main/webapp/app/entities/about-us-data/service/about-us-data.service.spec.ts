import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IAboutUsData, AboutUsData } from '../about-us-data.model';

import { AboutUsDataService } from './about-us-data.service';

describe('AboutUsData Service', () => {
  let service: AboutUsDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IAboutUsData;
  let expectedResult: IAboutUsData | IAboutUsData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AboutUsDataService);
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

    it('should create a AboutUsData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AboutUsData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AboutUsData', () => {
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

    it('should partial update a AboutUsData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          content: 'BBBBBB',
        },
        new AboutUsData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AboutUsData', () => {
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

    it('should delete a AboutUsData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAboutUsDataToCollectionIfMissing', () => {
      it('should add a AboutUsData to an empty array', () => {
        const aboutUsData: IAboutUsData = { id: 123 };
        expectedResult = service.addAboutUsDataToCollectionIfMissing([], aboutUsData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUsData);
      });

      it('should not add a AboutUsData to an array that contains it', () => {
        const aboutUsData: IAboutUsData = { id: 123 };
        const aboutUsDataCollection: IAboutUsData[] = [
          {
            ...aboutUsData,
          },
          { id: 456 },
        ];
        expectedResult = service.addAboutUsDataToCollectionIfMissing(aboutUsDataCollection, aboutUsData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AboutUsData to an array that doesn't contain it", () => {
        const aboutUsData: IAboutUsData = { id: 123 };
        const aboutUsDataCollection: IAboutUsData[] = [{ id: 456 }];
        expectedResult = service.addAboutUsDataToCollectionIfMissing(aboutUsDataCollection, aboutUsData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUsData);
      });

      it('should add only unique AboutUsData to an array', () => {
        const aboutUsDataArray: IAboutUsData[] = [{ id: 123 }, { id: 456 }, { id: 88704 }];
        const aboutUsDataCollection: IAboutUsData[] = [{ id: 123 }];
        expectedResult = service.addAboutUsDataToCollectionIfMissing(aboutUsDataCollection, ...aboutUsDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aboutUsData: IAboutUsData = { id: 123 };
        const aboutUsData2: IAboutUsData = { id: 456 };
        expectedResult = service.addAboutUsDataToCollectionIfMissing([], aboutUsData, aboutUsData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUsData);
        expect(expectedResult).toContain(aboutUsData2);
      });

      it('should accept null and undefined values', () => {
        const aboutUsData: IAboutUsData = { id: 123 };
        expectedResult = service.addAboutUsDataToCollectionIfMissing([], null, aboutUsData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUsData);
      });

      it('should return initial array if no AboutUsData is added', () => {
        const aboutUsDataCollection: IAboutUsData[] = [{ id: 123 }];
        expectedResult = service.addAboutUsDataToCollectionIfMissing(aboutUsDataCollection, undefined, null);
        expect(expectedResult).toEqual(aboutUsDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
