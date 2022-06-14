import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';
import { IRestaurantMenuData, RestaurantMenuData } from '../restaurant-menu-data.model';
import { IRestaurantMenu } from 'app/entities/restaurant-menu/restaurant-menu.model';
import { RestaurantMenuService } from 'app/entities/restaurant-menu/service/restaurant-menu.service';

import { RestaurantMenuDataUpdateComponent } from './restaurant-menu-data-update.component';

describe('RestaurantMenuData Management Update Component', () => {
  let comp: RestaurantMenuDataUpdateComponent;
  let fixture: ComponentFixture<RestaurantMenuDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let restaurantMenuDataService: RestaurantMenuDataService;
  let restaurantMenuService: RestaurantMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RestaurantMenuDataUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RestaurantMenuDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantMenuDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    restaurantMenuDataService = TestBed.inject(RestaurantMenuDataService);
    restaurantMenuService = TestBed.inject(RestaurantMenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RestaurantMenu query and add missing value', () => {
      const restaurantMenuData: IRestaurantMenuData = { id: 456 };
      const restaurantMenu: IRestaurantMenu = { id: 42025 };
      restaurantMenuData.restaurantMenu = restaurantMenu;

      const restaurantMenuCollection: IRestaurantMenu[] = [{ id: 3191 }];
      jest.spyOn(restaurantMenuService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantMenuCollection })));
      const additionalRestaurantMenus = [restaurantMenu];
      const expectedCollection: IRestaurantMenu[] = [...additionalRestaurantMenus, ...restaurantMenuCollection];
      jest.spyOn(restaurantMenuService, 'addRestaurantMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ restaurantMenuData });
      comp.ngOnInit();

      expect(restaurantMenuService.query).toHaveBeenCalled();
      expect(restaurantMenuService.addRestaurantMenuToCollectionIfMissing).toHaveBeenCalledWith(
        restaurantMenuCollection,
        ...additionalRestaurantMenus
      );
      expect(comp.restaurantMenusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const restaurantMenuData: IRestaurantMenuData = { id: 456 };
      const restaurantMenu: IRestaurantMenu = { id: 17670 };
      restaurantMenuData.restaurantMenu = restaurantMenu;

      activatedRoute.data = of({ restaurantMenuData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(restaurantMenuData));
      expect(comp.restaurantMenusSharedCollection).toContain(restaurantMenu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantMenuData>>();
      const restaurantMenuData = { id: 123 };
      jest.spyOn(restaurantMenuDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantMenuData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantMenuData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(restaurantMenuDataService.update).toHaveBeenCalledWith(restaurantMenuData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantMenuData>>();
      const restaurantMenuData = new RestaurantMenuData();
      jest.spyOn(restaurantMenuDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantMenuData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: restaurantMenuData }));
      saveSubject.complete();

      // THEN
      expect(restaurantMenuDataService.create).toHaveBeenCalledWith(restaurantMenuData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RestaurantMenuData>>();
      const restaurantMenuData = { id: 123 };
      jest.spyOn(restaurantMenuDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ restaurantMenuData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(restaurantMenuDataService.update).toHaveBeenCalledWith(restaurantMenuData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRestaurantMenuById', () => {
      it('Should return tracked RestaurantMenu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurantMenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
