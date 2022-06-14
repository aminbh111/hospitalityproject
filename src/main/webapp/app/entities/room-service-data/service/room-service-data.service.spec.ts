import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IRoomServiceData, RoomServiceData } from '../room-service-data.model';

import { RoomServiceDataService } from './room-service-data.service';

describe('RoomServiceData Service', () => {
  let service: RoomServiceDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IRoomServiceData;
  let expectedResult: IRoomServiceData | IRoomServiceData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoomServiceDataService);
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

    it('should create a RoomServiceData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RoomServiceData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoomServiceData', () => {
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

    it('should partial update a RoomServiceData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          title: 'BBBBBB',
          image: 'BBBBBB',
        },
        new RoomServiceData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoomServiceData', () => {
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

    it('should delete a RoomServiceData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRoomServiceDataToCollectionIfMissing', () => {
      it('should add a RoomServiceData to an empty array', () => {
        const roomServiceData: IRoomServiceData = { id: 123 };
        expectedResult = service.addRoomServiceDataToCollectionIfMissing([], roomServiceData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomServiceData);
      });

      it('should not add a RoomServiceData to an array that contains it', () => {
        const roomServiceData: IRoomServiceData = { id: 123 };
        const roomServiceDataCollection: IRoomServiceData[] = [
          {
            ...roomServiceData,
          },
          { id: 456 },
        ];
        expectedResult = service.addRoomServiceDataToCollectionIfMissing(roomServiceDataCollection, roomServiceData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoomServiceData to an array that doesn't contain it", () => {
        const roomServiceData: IRoomServiceData = { id: 123 };
        const roomServiceDataCollection: IRoomServiceData[] = [{ id: 456 }];
        expectedResult = service.addRoomServiceDataToCollectionIfMissing(roomServiceDataCollection, roomServiceData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomServiceData);
      });

      it('should add only unique RoomServiceData to an array', () => {
        const roomServiceDataArray: IRoomServiceData[] = [{ id: 123 }, { id: 456 }, { id: 60536 }];
        const roomServiceDataCollection: IRoomServiceData[] = [{ id: 123 }];
        expectedResult = service.addRoomServiceDataToCollectionIfMissing(roomServiceDataCollection, ...roomServiceDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roomServiceData: IRoomServiceData = { id: 123 };
        const roomServiceData2: IRoomServiceData = { id: 456 };
        expectedResult = service.addRoomServiceDataToCollectionIfMissing([], roomServiceData, roomServiceData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomServiceData);
        expect(expectedResult).toContain(roomServiceData2);
      });

      it('should accept null and undefined values', () => {
        const roomServiceData: IRoomServiceData = { id: 123 };
        expectedResult = service.addRoomServiceDataToCollectionIfMissing([], null, roomServiceData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomServiceData);
      });

      it('should return initial array if no RoomServiceData is added', () => {
        const roomServiceDataCollection: IRoomServiceData[] = [{ id: 123 }];
        expectedResult = service.addRoomServiceDataToCollectionIfMissing(roomServiceDataCollection, undefined, null);
        expect(expectedResult).toEqual(roomServiceDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
