import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRoomService, RoomService } from '../room-service.model';
import { RoomServiceService } from '../service/room-service.service';

import { RoomServiceRoutingResolveService } from './room-service-routing-resolve.service';

describe('RoomService routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RoomServiceRoutingResolveService;
  let service: RoomServiceService;
  let resultRoomService: IRoomService | undefined;

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
    routingResolveService = TestBed.inject(RoomServiceRoutingResolveService);
    service = TestBed.inject(RoomServiceService);
    resultRoomService = undefined;
  });

  describe('resolve', () => {
    it('should return IRoomService returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoomService = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRoomService).toEqual({ id: 123 });
    });

    it('should return new IRoomService if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoomService = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRoomService).toEqual(new RoomService());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RoomService })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoomService = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRoomService).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
