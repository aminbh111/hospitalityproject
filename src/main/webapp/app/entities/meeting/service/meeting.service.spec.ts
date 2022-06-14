import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IMeeting, Meeting } from '../meeting.model';

import { MeetingService } from './meeting.service';

describe('Meeting Service', () => {
  let service: MeetingService;
  let httpMock: HttpTestingController;
  let elemDefault: IMeeting;
  let expectedResult: IMeeting | IMeeting[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MeetingService);
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

    it('should create a Meeting', () => {
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

      service.create(new Meeting()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Meeting', () => {
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

    it('should partial update a Meeting', () => {
      const patchObject = Object.assign(
        {
          contentPosition: 'BBBBBB',
        },
        new Meeting()
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

    it('should return a list of Meeting', () => {
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

    it('should delete a Meeting', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMeetingToCollectionIfMissing', () => {
      it('should add a Meeting to an empty array', () => {
        const meeting: IMeeting = { id: 123 };
        expectedResult = service.addMeetingToCollectionIfMissing([], meeting);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meeting);
      });

      it('should not add a Meeting to an array that contains it', () => {
        const meeting: IMeeting = { id: 123 };
        const meetingCollection: IMeeting[] = [
          {
            ...meeting,
          },
          { id: 456 },
        ];
        expectedResult = service.addMeetingToCollectionIfMissing(meetingCollection, meeting);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Meeting to an array that doesn't contain it", () => {
        const meeting: IMeeting = { id: 123 };
        const meetingCollection: IMeeting[] = [{ id: 456 }];
        expectedResult = service.addMeetingToCollectionIfMissing(meetingCollection, meeting);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meeting);
      });

      it('should add only unique Meeting to an array', () => {
        const meetingArray: IMeeting[] = [{ id: 123 }, { id: 456 }, { id: 868 }];
        const meetingCollection: IMeeting[] = [{ id: 123 }];
        expectedResult = service.addMeetingToCollectionIfMissing(meetingCollection, ...meetingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const meeting: IMeeting = { id: 123 };
        const meeting2: IMeeting = { id: 456 };
        expectedResult = service.addMeetingToCollectionIfMissing([], meeting, meeting2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(meeting);
        expect(expectedResult).toContain(meeting2);
      });

      it('should accept null and undefined values', () => {
        const meeting: IMeeting = { id: 123 };
        expectedResult = service.addMeetingToCollectionIfMissing([], null, meeting, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(meeting);
      });

      it('should return initial array if no Meeting is added', () => {
        const meetingCollection: IMeeting[] = [{ id: 123 }];
        expectedResult = service.addMeetingToCollectionIfMissing(meetingCollection, undefined, null);
        expect(expectedResult).toEqual(meetingCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
