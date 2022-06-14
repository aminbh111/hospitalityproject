import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IMeetingData, MeetingData } from '../meeting-data.model';

import { MeetingDataService } from './meeting-data.service';

describe('MeetingData Service', () => {
  let service: MeetingDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IMeetingData;
  let expectedResult: IMeetingData | IMeetingData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeetingDataService);
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

    it('should create a MeetingData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MeetingData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MeetingData', () => {
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

    it('should partial update a MeetingData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          title: 'BBBBBB',
          image: 'BBBBBB',
        },
        new MeetingData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MeetingData', () => {
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

    it('should delete a MeetingData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMeetingDataToCollectionIfMissing', () => {
      it('should add a MeetingData to an empty array', () => {
        const meetingData: IMeetingData = { id: 123 };
        expectedResult = service.addMeetingDataToCollectionIfMissing([], meetingData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meetingData);
      });

      it('should not add a MeetingData to an array that contains it', () => {
        const meetingData: IMeetingData = { id: 123 };
        const meetingDataCollection: IMeetingData[] = [
          {
            ...meetingData,
          },
          { id: 456 },
        ];
        expectedResult = service.addMeetingDataToCollectionIfMissing(meetingDataCollection, meetingData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MeetingData to an array that doesn't contain it", () => {
        const meetingData: IMeetingData = { id: 123 };
        const meetingDataCollection: IMeetingData[] = [{ id: 456 }];
        expectedResult = service.addMeetingDataToCollectionIfMissing(meetingDataCollection, meetingData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meetingData);
      });

      it('should add only unique MeetingData to an array', () => {
        const meetingDataArray: IMeetingData[] = [{ id: 123 }, { id: 456 }, { id: 22735 }];
        const meetingDataCollection: IMeetingData[] = [{ id: 123 }];
        expectedResult = service.addMeetingDataToCollectionIfMissing(meetingDataCollection, ...meetingDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const meetingData: IMeetingData = { id: 123 };
        const meetingData2: IMeetingData = { id: 456 };
        expectedResult = service.addMeetingDataToCollectionIfMissing([], meetingData, meetingData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meetingData);
        expect(expectedResult).toContain(meetingData2);
      });

      it('should accept null and undefined values', () => {
        const meetingData: IMeetingData = { id: 123 };
        expectedResult = service.addMeetingDataToCollectionIfMissing([], null, meetingData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meetingData);
      });

      it('should return initial array if no MeetingData is added', () => {
        const meetingDataCollection: IMeetingData[] = [{ id: 123 }];
        expectedResult = service.addMeetingDataToCollectionIfMissing(meetingDataCollection, undefined, null);
        expect(expectedResult).toEqual(meetingDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
