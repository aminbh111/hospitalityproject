import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IAboutUs, AboutUs } from '../about-us.model';

import { AboutUsService } from './about-us.service';

describe('AboutUs Service', () => {
  let service: AboutUsService;
  let httpMock: HttpTestingController;
  let elemDefault: IAboutUs;
  let expectedResult: IAboutUs | IAboutUs[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AboutUsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      publish: false,
      contentPosition: Position.LEFT,
      imagePosition: Position.LEFT,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a AboutUs', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new AboutUs()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AboutUs', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          publish: true,
          contentPosition: 'BBBBBB',
          imagePosition: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AboutUs', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
          imagePosition: 'BBBBBB',
        },
        new AboutUs()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AboutUs', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          publish: true,
          contentPosition: 'BBBBBB',
          imagePosition: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a AboutUs', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAboutUsToCollectionIfMissing', () => {
      it('should add a AboutUs to an empty array', () => {
        const aboutUs: IAboutUs = { id: 123 };
        expectedResult = service.addAboutUsToCollectionIfMissing([], aboutUs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should not add a AboutUs to an array that contains it', () => {
        const aboutUs: IAboutUs = { id: 123 };
        const aboutUsCollection: IAboutUs[] = [
          {
            ...aboutUs,
          },
          { id: 456 },
        ];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, aboutUs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AboutUs to an array that doesn't contain it", () => {
        const aboutUs: IAboutUs = { id: 123 };
        const aboutUsCollection: IAboutUs[] = [{ id: 456 }];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, aboutUs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should add only unique AboutUs to an array', () => {
        const aboutUsArray: IAboutUs[] = [{ id: 123 }, { id: 456 }, { id: 83322 }];
        const aboutUsCollection: IAboutUs[] = [{ id: 123 }];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, ...aboutUsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aboutUs: IAboutUs = { id: 123 };
        const aboutUs2: IAboutUs = { id: 456 };
        expectedResult = service.addAboutUsToCollectionIfMissing([], aboutUs, aboutUs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aboutUs);
        expect(expectedResult).toContain(aboutUs2);
      });

      it('should accept null and undefined values', () => {
        const aboutUs: IAboutUs = { id: 123 };
        expectedResult = service.addAboutUsToCollectionIfMissing([], null, aboutUs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aboutUs);
      });

      it('should return initial array if no AboutUs is added', () => {
        const aboutUsCollection: IAboutUs[] = [{ id: 123 }];
        expectedResult = service.addAboutUsToCollectionIfMissing(aboutUsCollection, undefined, null);
        expect(expectedResult).toEqual(aboutUsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
