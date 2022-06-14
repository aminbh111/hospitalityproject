import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IContactUs, ContactUs } from '../contact-us.model';

import { ContactUsService } from './contact-us.service';

describe('ContactUs Service', () => {
  let service: ContactUsService;
  let httpMock: HttpTestingController;
  let elemDefault: IContactUs;
  let expectedResult: IContactUs | IContactUs[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContactUsService);
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

    it('should create a ContactUs', () => {
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

      service.create(new ContactUs()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactUs', () => {
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

    it('should partial update a ContactUs', () => {
      const patchObject = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
          contentPosition: 'BBBBBB',
          imagePosition: 'BBBBBB',
        },
        new ContactUs()
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

    it('should return a list of ContactUs', () => {
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

    it('should delete a ContactUs', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContactUsToCollectionIfMissing', () => {
      it('should add a ContactUs to an empty array', () => {
        const contactUs: IContactUs = { id: 123 };
        expectedResult = service.addContactUsToCollectionIfMissing([], contactUs);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUs);
      });

      it('should not add a ContactUs to an array that contains it', () => {
        const contactUs: IContactUs = { id: 123 };
        const contactUsCollection: IContactUs[] = [
          {
            ...contactUs,
          },
          { id: 456 },
        ];
        expectedResult = service.addContactUsToCollectionIfMissing(contactUsCollection, contactUs);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactUs to an array that doesn't contain it", () => {
        const contactUs: IContactUs = { id: 123 };
        const contactUsCollection: IContactUs[] = [{ id: 456 }];
        expectedResult = service.addContactUsToCollectionIfMissing(contactUsCollection, contactUs);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUs);
      });

      it('should add only unique ContactUs to an array', () => {
        const contactUsArray: IContactUs[] = [{ id: 123 }, { id: 456 }, { id: 69734 }];
        const contactUsCollection: IContactUs[] = [{ id: 123 }];
        expectedResult = service.addContactUsToCollectionIfMissing(contactUsCollection, ...contactUsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactUs: IContactUs = { id: 123 };
        const contactUs2: IContactUs = { id: 456 };
        expectedResult = service.addContactUsToCollectionIfMissing([], contactUs, contactUs2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUs);
        expect(expectedResult).toContain(contactUs2);
      });

      it('should accept null and undefined values', () => {
        const contactUs: IContactUs = { id: 123 };
        expectedResult = service.addContactUsToCollectionIfMissing([], null, contactUs, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUs);
      });

      it('should return initial array if no ContactUs is added', () => {
        const contactUsCollection: IContactUs[] = [{ id: 123 }];
        expectedResult = service.addContactUsToCollectionIfMissing(contactUsCollection, undefined, null);
        expect(expectedResult).toEqual(contactUsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
