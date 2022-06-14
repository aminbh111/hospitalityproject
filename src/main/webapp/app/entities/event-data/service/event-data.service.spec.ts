import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IEventData, EventData } from '../event-data.model';

import { EventDataService } from './event-data.service';

describe('EventData Service', () => {
  let service: EventDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IEventData;
  let expectedResult: IEventData | IEventData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EventDataService);
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

    it('should create a EventData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new EventData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EventData', () => {
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

    it('should partial update a EventData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          title: 'BBBBBB',
          content: 'BBBBBB',
        },
        new EventData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EventData', () => {
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

    it('should delete a EventData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEventDataToCollectionIfMissing', () => {
      it('should add a EventData to an empty array', () => {
        const eventData: IEventData = { id: 123 };
        expectedResult = service.addEventDataToCollectionIfMissing([], eventData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventData);
      });

      it('should not add a EventData to an array that contains it', () => {
        const eventData: IEventData = { id: 123 };
        const eventDataCollection: IEventData[] = [
          {
            ...eventData,
          },
          { id: 456 },
        ];
        expectedResult = service.addEventDataToCollectionIfMissing(eventDataCollection, eventData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EventData to an array that doesn't contain it", () => {
        const eventData: IEventData = { id: 123 };
        const eventDataCollection: IEventData[] = [{ id: 456 }];
        expectedResult = service.addEventDataToCollectionIfMissing(eventDataCollection, eventData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventData);
      });

      it('should add only unique EventData to an array', () => {
        const eventDataArray: IEventData[] = [{ id: 123 }, { id: 456 }, { id: 67451 }];
        const eventDataCollection: IEventData[] = [{ id: 123 }];
        expectedResult = service.addEventDataToCollectionIfMissing(eventDataCollection, ...eventDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const eventData: IEventData = { id: 123 };
        const eventData2: IEventData = { id: 456 };
        expectedResult = service.addEventDataToCollectionIfMissing([], eventData, eventData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(eventData);
        expect(expectedResult).toContain(eventData2);
      });

      it('should accept null and undefined values', () => {
        const eventData: IEventData = { id: 123 };
        expectedResult = service.addEventDataToCollectionIfMissing([], null, eventData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(eventData);
      });

      it('should return initial array if no EventData is added', () => {
        const eventDataCollection: IEventData[] = [{ id: 123 }];
        expectedResult = service.addEventDataToCollectionIfMissing(eventDataCollection, undefined, null);
        expect(expectedResult).toEqual(eventDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
