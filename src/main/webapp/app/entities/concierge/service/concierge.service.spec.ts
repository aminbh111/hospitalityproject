import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IConcierge, Concierge } from '../concierge.model';

import { ConciergeService } from './concierge.service';

describe('Concierge Service', () => {
  let service: ConciergeService;
  let httpMock: HttpTestingController;
  let elemDefault: IConcierge;
  let expectedResult: IConcierge | IConcierge[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ConciergeService);
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

    it('should create a Concierge', () => {
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

      service.create(new Concierge()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Concierge', () => {
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

    it('should partial update a Concierge', () => {
      const patchObject = Object.assign(
        {
          publish: true,
          contentPosition: 'BBBBBB',
        },
        new Concierge()
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

    it('should return a list of Concierge', () => {
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

    it('should delete a Concierge', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addConciergeToCollectionIfMissing', () => {
      it('should add a Concierge to an empty array', () => {
        const concierge: IConcierge = { id: 123 };
        expectedResult = service.addConciergeToCollectionIfMissing([], concierge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(concierge);
      });

      it('should not add a Concierge to an array that contains it', () => {
        const concierge: IConcierge = { id: 123 };
        const conciergeCollection: IConcierge[] = [
          {
            ...concierge,
          },
          { id: 456 },
        ];
        expectedResult = service.addConciergeToCollectionIfMissing(conciergeCollection, concierge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Concierge to an array that doesn't contain it", () => {
        const concierge: IConcierge = { id: 123 };
        const conciergeCollection: IConcierge[] = [{ id: 456 }];
        expectedResult = service.addConciergeToCollectionIfMissing(conciergeCollection, concierge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(concierge);
      });

      it('should add only unique Concierge to an array', () => {
        const conciergeArray: IConcierge[] = [{ id: 123 }, { id: 456 }, { id: 56729 }];
        const conciergeCollection: IConcierge[] = [{ id: 123 }];
        expectedResult = service.addConciergeToCollectionIfMissing(conciergeCollection, ...conciergeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const concierge: IConcierge = { id: 123 };
        const concierge2: IConcierge = { id: 456 };
        expectedResult = service.addConciergeToCollectionIfMissing([], concierge, concierge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(concierge);
        expect(expectedResult).toContain(concierge2);
      });

      it('should accept null and undefined values', () => {
        const concierge: IConcierge = { id: 123 };
        expectedResult = service.addConciergeToCollectionIfMissing([], null, concierge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(concierge);
      });

      it('should return initial array if no Concierge is added', () => {
        const conciergeCollection: IConcierge[] = [{ id: 123 }];
        expectedResult = service.addConciergeToCollectionIfMissing(conciergeCollection, undefined, null);
        expect(expectedResult).toEqual(conciergeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
