import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Language } from 'app/entities/enumerations/language.model';
import { IRestaurantMenuData, RestaurantMenuData } from '../restaurant-menu-data.model';

import { RestaurantMenuDataService } from './restaurant-menu-data.service';

describe('RestaurantMenuData Service', () => {
  let service: RestaurantMenuDataService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurantMenuData;
  let expectedResult: IRestaurantMenuData | IRestaurantMenuData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurantMenuDataService);
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

    it('should create a RestaurantMenuData', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RestaurantMenuData()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RestaurantMenuData', () => {
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

    it('should partial update a RestaurantMenuData', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          image: 'BBBBBB',
        },
        new RestaurantMenuData()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RestaurantMenuData', () => {
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

    it('should delete a RestaurantMenuData', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurantMenuDataToCollectionIfMissing', () => {
      it('should add a RestaurantMenuData to an empty array', () => {
        const restaurantMenuData: IRestaurantMenuData = { id: 123 };
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing([], restaurantMenuData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantMenuData);
      });

      it('should not add a RestaurantMenuData to an array that contains it', () => {
        const restaurantMenuData: IRestaurantMenuData = { id: 123 };
        const restaurantMenuDataCollection: IRestaurantMenuData[] = [
          {
            ...restaurantMenuData,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing(restaurantMenuDataCollection, restaurantMenuData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RestaurantMenuData to an array that doesn't contain it", () => {
        const restaurantMenuData: IRestaurantMenuData = { id: 123 };
        const restaurantMenuDataCollection: IRestaurantMenuData[] = [{ id: 456 }];
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing(restaurantMenuDataCollection, restaurantMenuData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantMenuData);
      });

      it('should add only unique RestaurantMenuData to an array', () => {
        const restaurantMenuDataArray: IRestaurantMenuData[] = [{ id: 123 }, { id: 456 }, { id: 92520 }];
        const restaurantMenuDataCollection: IRestaurantMenuData[] = [{ id: 123 }];
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing(restaurantMenuDataCollection, ...restaurantMenuDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurantMenuData: IRestaurantMenuData = { id: 123 };
        const restaurantMenuData2: IRestaurantMenuData = { id: 456 };
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing([], restaurantMenuData, restaurantMenuData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantMenuData);
        expect(expectedResult).toContain(restaurantMenuData2);
      });

      it('should accept null and undefined values', () => {
        const restaurantMenuData: IRestaurantMenuData = { id: 123 };
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing([], null, restaurantMenuData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantMenuData);
      });

      it('should return initial array if no RestaurantMenuData is added', () => {
        const restaurantMenuDataCollection: IRestaurantMenuData[] = [{ id: 123 }];
        expectedResult = service.addRestaurantMenuDataToCollectionIfMissing(restaurantMenuDataCollection, undefined, null);
        expect(expectedResult).toEqual(restaurantMenuDataCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
