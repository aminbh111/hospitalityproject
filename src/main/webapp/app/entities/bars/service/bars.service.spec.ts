import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IBars, Bars } from '../bars.model';

import { BarsService } from './bars.service';

describe('Bars Service', () => {
  let service: BarsService;
  let httpMock: HttpTestingController;
  let elemDefault: IBars;
  let expectedResult: IBars | IBars[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BarsService);
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

    it('should create a Bars', () => {
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

      service.create(new Bars()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bars', () => {
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

    it('should partial update a Bars', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        new Bars()
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

    it('should return a list of Bars', () => {
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

    it('should delete a Bars', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBarsToCollectionIfMissing', () => {
      it('should add a Bars to an empty array', () => {
        const bars: IBars = { id: 123 };
        expectedResult = service.addBarsToCollectionIfMissing([], bars);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bars);
      });

      it('should not add a Bars to an array that contains it', () => {
        const bars: IBars = { id: 123 };
        const barsCollection: IBars[] = [
          {
            ...bars,
          },
          { id: 456 },
        ];
        expectedResult = service.addBarsToCollectionIfMissing(barsCollection, bars);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bars to an array that doesn't contain it", () => {
        const bars: IBars = { id: 123 };
        const barsCollection: IBars[] = [{ id: 456 }];
        expectedResult = service.addBarsToCollectionIfMissing(barsCollection, bars);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bars);
      });

      it('should add only unique Bars to an array', () => {
        const barsArray: IBars[] = [{ id: 123 }, { id: 456 }, { id: 24085 }];
        const barsCollection: IBars[] = [{ id: 123 }];
        expectedResult = service.addBarsToCollectionIfMissing(barsCollection, ...barsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bars: IBars = { id: 123 };
        const bars2: IBars = { id: 456 };
        expectedResult = service.addBarsToCollectionIfMissing([], bars, bars2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bars);
        expect(expectedResult).toContain(bars2);
      });

      it('should accept null and undefined values', () => {
        const bars: IBars = { id: 123 };
        expectedResult = service.addBarsToCollectionIfMissing([], null, bars, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bars);
      });

      it('should return initial array if no Bars is added', () => {
        const barsCollection: IBars[] = [{ id: 123 }];
        expectedResult = service.addBarsToCollectionIfMissing(barsCollection, undefined, null);
        expect(expectedResult).toEqual(barsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
