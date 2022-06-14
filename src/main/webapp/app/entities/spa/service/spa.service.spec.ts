import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { ISpa, Spa } from '../spa.model';

import { SpaService } from './spa.service';

describe('Spa Service', () => {
  let service: SpaService;
  let httpMock: HttpTestingController;
  let elemDefault: ISpa;
  let expectedResult: ISpa | ISpa[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SpaService);
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

    it('should create a Spa', () => {
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

      service.create(new Spa()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Spa', () => {
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

    it('should partial update a Spa', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new Spa()
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

    it('should return a list of Spa', () => {
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

    it('should delete a Spa', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSpaToCollectionIfMissing', () => {
      it('should add a Spa to an empty array', () => {
        const spa: ISpa = { id: 123 };
        expectedResult = service.addSpaToCollectionIfMissing([], spa);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spa);
      });

      it('should not add a Spa to an array that contains it', () => {
        const spa: ISpa = { id: 123 };
        const spaCollection: ISpa[] = [
          {
            ...spa,
          },
          { id: 456 },
        ];
        expectedResult = service.addSpaToCollectionIfMissing(spaCollection, spa);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Spa to an array that doesn't contain it", () => {
        const spa: ISpa = { id: 123 };
        const spaCollection: ISpa[] = [{ id: 456 }];
        expectedResult = service.addSpaToCollectionIfMissing(spaCollection, spa);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spa);
      });

      it('should add only unique Spa to an array', () => {
        const spaArray: ISpa[] = [{ id: 123 }, { id: 456 }, { id: 52379 }];
        const spaCollection: ISpa[] = [{ id: 123 }];
        expectedResult = service.addSpaToCollectionIfMissing(spaCollection, ...spaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const spa: ISpa = { id: 123 };
        const spa2: ISpa = { id: 456 };
        expectedResult = service.addSpaToCollectionIfMissing([], spa, spa2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(spa);
        expect(expectedResult).toContain(spa2);
      });

      it('should accept null and undefined values', () => {
        const spa: ISpa = { id: 123 };
        expectedResult = service.addSpaToCollectionIfMissing([], null, spa, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(spa);
      });

      it('should return initial array if no Spa is added', () => {
        const spaCollection: ISpa[] = [{ id: 123 }];
        expectedResult = service.addSpaToCollectionIfMissing(spaCollection, undefined, null);
        expect(expectedResult).toEqual(spaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
