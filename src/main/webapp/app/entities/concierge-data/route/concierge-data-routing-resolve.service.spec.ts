import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IConciergeData, ConciergeData } from '../concierge-data.model';
import { ConciergeDataService } from '../service/concierge-data.service';

import { ConciergeDataRoutingResolveService } from './concierge-data-routing-resolve.service';

describe('ConciergeData routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ConciergeDataRoutingResolveService;
  let service: ConciergeDataService;
  let resultConciergeData: IConciergeData | undefined;

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
    routingResolveService = TestBed.inject(ConciergeDataRoutingResolveService);
    service = TestBed.inject(ConciergeDataService);
    resultConciergeData = undefined;
  });

  describe('resolve', () => {
    it('should return IConciergeData returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConciergeData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConciergeData).toEqual({ id: 123 });
    });

    it('should return new IConciergeData if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConciergeData = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultConciergeData).toEqual(new ConciergeData());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ConciergeData })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConciergeData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConciergeData).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
