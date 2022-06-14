import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IMeetingData, MeetingData } from '../meeting-data.model';
import { MeetingDataService } from '../service/meeting-data.service';

import { MeetingDataRoutingResolveService } from './meeting-data-routing-resolve.service';

describe('MeetingData routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MeetingDataRoutingResolveService;
  let service: MeetingDataService;
  let resultMeetingData: IMeetingData | undefined;

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
    routingResolveService = TestBed.inject(MeetingDataRoutingResolveService);
    service = TestBed.inject(MeetingDataService);
    resultMeetingData = undefined;
  });

  describe('resolve', () => {
    it('should return IMeetingData returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeetingData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMeetingData).toEqual({ id: 123 });
    });

    it('should return new IMeetingData if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeetingData = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMeetingData).toEqual(new MeetingData());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MeetingData })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMeetingData = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMeetingData).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
