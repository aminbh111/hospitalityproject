import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Position } from 'app/entities/enumerations/position.model';
import { IRestaurantMenu, RestaurantMenu } from '../restaurant-menu.model';

import { RestaurantMenuService } from './restaurant-menu.service';

describe('RestaurantMenu Service', () => {
  let service: RestaurantMenuService;
  let httpMock: HttpTestingController;
  let elemDefault: IRestaurantMenu;
  let expectedResult: IRestaurantMenu | IRestaurantMenu[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RestaurantMenuService);
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

    it('should create a RestaurantMenu', () => {
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

      service.create(new RestaurantMenu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RestaurantMenu', () => {
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

    it('should partial update a RestaurantMenu', () => {
      const patchObject = Object.assign(
        {
          contentPosition: 'BBBBBB',
          imagePosition: 'BBBBBB',
        },
        new RestaurantMenu()
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

    it('should return a list of RestaurantMenu', () => {
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

    it('should delete a RestaurantMenu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRestaurantMenuToCollectionIfMissing', () => {
      it('should add a RestaurantMenu to an empty array', () => {
        const restaurantMenu: IRestaurantMenu = { id: 123 };
        expectedResult = service.addRestaurantMenuToCollectionIfMissing([], restaurantMenu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantMenu);
      });

      it('should not add a RestaurantMenu to an array that contains it', () => {
        const restaurantMenu: IRestaurantMenu = { id: 123 };
        const restaurantMenuCollection: IRestaurantMenu[] = [
          {
            ...restaurantMenu,
          },
          { id: 456 },
        ];
        expectedResult = service.addRestaurantMenuToCollectionIfMissing(restaurantMenuCollection, restaurantMenu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RestaurantMenu to an array that doesn't contain it", () => {
        const restaurantMenu: IRestaurantMenu = { id: 123 };
        const restaurantMenuCollection: IRestaurantMenu[] = [{ id: 456 }];
        expectedResult = service.addRestaurantMenuToCollectionIfMissing(restaurantMenuCollection, restaurantMenu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantMenu);
      });

      it('should add only unique RestaurantMenu to an array', () => {
        const restaurantMenuArray: IRestaurantMenu[] = [{ id: 123 }, { id: 456 }, { id: 92320 }];
        const restaurantMenuCollection: IRestaurantMenu[] = [{ id: 123 }];
        expectedResult = service.addRestaurantMenuToCollectionIfMissing(restaurantMenuCollection, ...restaurantMenuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const restaurantMenu: IRestaurantMenu = { id: 123 };
        const restaurantMenu2: IRestaurantMenu = { id: 456 };
        expectedResult = service.addRestaurantMenuToCollectionIfMissing([], restaurantMenu, restaurantMenu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(restaurantMenu);
        expect(expectedResult).toContain(restaurantMenu2);
      });

      it('should accept null and undefined values', () => {
        const restaurantMenu: IRestaurantMenu = { id: 123 };
        expectedResult = service.addRestaurantMenuToCollectionIfMissing([], null, restaurantMenu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(restaurantMenu);
      });

      it('should return initial array if no RestaurantMenu is added', () => {
        const restaurantMenuCollection: IRestaurantMenu[] = [{ id: 123 }];
        expectedResult = service.addRestaurantMenuToCollectionIfMissing(restaurantMenuCollection, undefined, null);
        expect(expectedResult).toEqual(restaurantMenuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
