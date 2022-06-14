import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IContactUsData, ContactUsData } from '../contact-us-data.model';

import { ContactUsDataService } from './contact-us-data.service';

describe('ContactUsData Service', () => {
  let service: ContactUsDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IContactUsData;
  let expectedResult: IContactUsData | IContactUsData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContactUsDataService);
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

    it('should create a ContactUsData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ContactUsData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ContactUsData', () => {
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

    it('should partial update a ContactUsData', () => {
      const patchObject = Object.assign(
        {
          lang: 'BBBBBB',
          content: 'BBBBBB',
        },
        new ContactUsData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ContactUsData', () => {
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

    it('should delete a ContactUsData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContactUsDataToCollectionIfMissing', () => {
      it('should add a ContactUsData to an empty array', () => {
        const contactUsData: IContactUsData = { id: 123 };
        expectedResult = service.addContactUsDataToCollectionIfMissing([], contactUsData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUsData);
      });

      it('should not add a ContactUsData to an array that contains it', () => {
        const contactUsData: IContactUsData = { id: 123 };
        const contactUsDataCollection: IContactUsData[] = [
          {
            ...contactUsData,
          },
          { id: 456 },
        ];
        expectedResult = service.addContactUsDataToCollectionIfMissing(contactUsDataCollection, contactUsData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ContactUsData to an array that doesn't contain it", () => {
        const contactUsData: IContactUsData = { id: 123 };
        const contactUsDataCollection: IContactUsData[] = [{ id: 456 }];
        expectedResult = service.addContactUsDataToCollectionIfMissing(contactUsDataCollection, contactUsData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUsData);
      });

      it('should add only unique ContactUsData to an array', () => {
        const contactUsDataArray: IContactUsData[] = [{ id: 123 }, { id: 456 }, { id: 86477 }];
        const contactUsDataCollection: IContactUsData[] = [{ id: 123 }];
        expectedResult = service.addContactUsDataToCollectionIfMissing(contactUsDataCollection, ...contactUsDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contactUsData: IContactUsData = { id: 123 };
        const contactUsData2: IContactUsData = { id: 456 };
        expectedResult = service.addContactUsDataToCollectionIfMissing([], contactUsData, contactUsData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contactUsData);
        expect(expectedResult).toContain(contactUsData2);
      });

      it('should accept null and undefined values', () => {
        const contactUsData: IContactUsData = { id: 123 };
        expectedResult = service.addContactUsDataToCollectionIfMissing([], null, contactUsData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(contactUsData);
      });

      it('should return initial array if no ContactUsData is added', () => {
        const contactUsDataCollection: IContactUsData[] = [{ id: 123 }];
        expectedResult = service.addContactUsDataToCollectionIfMissing(contactUsDataCollection, undefined, null);
        expect(expectedResult).toEqual(contactUsDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
