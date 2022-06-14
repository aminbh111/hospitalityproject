import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAboutUsData, AboutUsData } from '../about-us-data.model';
import { AboutUsDataService } from '../service/about-us-data.service';

import { AboutUsDataRoutingResolveService } from './about-us-data-routing-resolve.service';

describe('AboutUsData routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AboutUsDataRoutingResolveService;
  let service: AboutUsDataService;
  let resultAboutUsData: IAboutUsData | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AboutUsDataRoutingResolveService);
    service = TestBed.inject(AboutUsDataService);
    resultAboutUsData = undefined;
  });

  describe('resolve', () => {
    it('should return IAboutUsData returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAboutUsData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAboutUsData).toEqual({ id: 123 });
    });

    it('should return new IAboutUsData if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAboutUsData = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAboutUsData).toEqual(new AboutUsData());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AboutUsData })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAboutUsData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAboutUsData).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
