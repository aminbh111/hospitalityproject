import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomServiceDataService } from '../service/room-service-data.service';
import { IRoomServiceData, RoomServiceData } from '../room-service-data.model';
import { IRoomService } from 'app/entities/room-service/room-service.model';
import { RoomServiceService } from 'app/entities/room-service/service/room-service.service';

import { RoomServiceDataUpdateComponent } from './room-service-data-update.component';

describe('RoomServiceData Management Update Component', () => {
  let comp: RoomServiceDataUpdateComponent;
  let fixture: ComponentFixture<RoomServiceDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomServiceDataService: RoomServiceDataService;
  let roomServiceService: RoomServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomServiceDataUpdateComponent],
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
      .overrideTemplate(RoomServiceDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomServiceDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomServiceDataService = TestBed.inject(RoomServiceDataService);
    roomServiceService = TestBed.inject(RoomServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RoomService query and add missing value', () => {
      const roomServiceData: IRoomServiceData = { id: 456 };
      const roomService: IRoomService = { id: 54622 };
      roomServiceData.roomService = roomService;

      const roomServiceCollection: IRoomService[] = [{ id: 72243 }];
      jest.spyOn(roomServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: roomServiceCollection })));
      const additionalRoomServices = [roomService];
      const expectedCollection: IRoomService[] = [...additionalRoomServices, ...roomServiceCollection];
      jest.spyOn(roomServiceService, 'addRoomServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ roomServiceData });
      comp.ngOnInit();

      expect(roomServiceService.query).toHaveBeenCalled();
      expect(roomServiceService.addRoomServiceToCollectionIfMissing).toHaveBeenCalledWith(roomServiceCollection, ...additionalRoomServices);
      expect(comp.roomServicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const roomServiceData: IRoomServiceData = { id: 456 };
      const roomService: IRoomService = { id: 71201 };
      roomServiceData.roomService = roomService;

      activatedRoute.data = of({ roomServiceData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(roomServiceData));
      expect(comp.roomServicesSharedCollection).toContain(roomService);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomServiceData>>();
      const roomServiceData = { id: 123 };
      jest.spyOn(roomServiceDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomServiceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomServiceData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomServiceDataService.update).toHaveBeenCalledWith(roomServiceData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomServiceData>>();
      const roomServiceData = new RoomServiceData();
      jest.spyOn(roomServiceDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomServiceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomServiceData }));
      saveSubject.complete();

      // THEN
      expect(roomServiceDataService.create).toHaveBeenCalledWith(roomServiceData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomServiceData>>();
      const roomServiceData = { id: 123 };
      jest.spyOn(roomServiceDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomServiceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomServiceDataService.update).toHaveBeenCalledWith(roomServiceData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRoomServiceById', () => {
      it('Should return tracked RoomService primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRoomServiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
