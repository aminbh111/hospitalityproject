import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IRoomService, RoomService } from '../room-service.model';

import { RoomServiceService } from './room-service.service';

describe('RoomService Service', () => {
  let service: RoomServiceService;
  let httpMock: HttpTestingController;
  let elemDefault: IRoomService;
  let expectedResult: IRoomService | IRoomService[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoomServiceService);
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

    it('should create a RoomService', () => {
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

      service.create(new RoomService()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoomService', () => {
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

    it('should partial update a RoomService', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
          contentPosition: 'BBBBBB',
        },
        new RoomService()
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

    it('should return a list of RoomService', () => {
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

    it('should delete a RoomService', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRoomServiceToCollectionIfMissing', () => {
      it('should add a RoomService to an empty array', () => {
        const roomService: IRoomService = { id: 123 };
        expectedResult = service.addRoomServiceToCollectionIfMissing([], roomService);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomService);
      });

      it('should not add a RoomService to an array that contains it', () => {
        const roomService: IRoomService = { id: 123 };
        const roomServiceCollection: IRoomService[] = [
          {
            ...roomService,
          },
          { id: 456 },
        ];
        expectedResult = service.addRoomServiceToCollectionIfMissing(roomServiceCollection, roomService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoomService to an array that doesn't contain it", () => {
        const roomService: IRoomService = { id: 123 };
        const roomServiceCollection: IRoomService[] = [{ id: 456 }];
        expectedResult = service.addRoomServiceToCollectionIfMissing(roomServiceCollection, roomService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomService);
      });

      it('should add only unique RoomService to an array', () => {
        const roomServiceArray: IRoomService[] = [{ id: 123 }, { id: 456 }, { id: 41691 }];
        const roomServiceCollection: IRoomService[] = [{ id: 123 }];
        expectedResult = service.addRoomServiceToCollectionIfMissing(roomServiceCollection, ...roomServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roomService: IRoomService = { id: 123 };
        const roomService2: IRoomService = { id: 456 };
        expectedResult = service.addRoomServiceToCollectionIfMissing([], roomService, roomService2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomService);
        expect(expectedResult).toContain(roomService2);
      });

      it('should accept null and undefined values', () => {
        const roomService: IRoomService = { id: 123 };
        expectedResult = service.addRoomServiceToCollectionIfMissing([], null, roomService, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomService);
      });

      it('should return initial array if no RoomService is added', () => {
        const roomServiceCollection: IRoomService[] = [{ id: 123 }];
        expectedResult = service.addRoomServiceToCollectionIfMissing(roomServiceCollection, undefined, null);
        expect(expectedResult).toEqual(roomServiceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
