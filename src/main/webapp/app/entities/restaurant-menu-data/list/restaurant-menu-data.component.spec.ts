import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RestaurantMenuDataService } from '../service/restaurant-menu-data.service';

import { RestaurantMenuDataComponent } from './restaurant-menu-data.component';

describe('RestaurantMenuData Management Component', () => {
  let comp: RestaurantMenuDataComponent;
  let fixture: ComponentFixture<RestaurantMenuDataComponent>;
  let service: RestaurantMenuDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurantMenuDataComponent],
    })
      .overrideTemplate(RestaurantMenuDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantMenuDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurantMenuDataService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.restaurantMenuData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
