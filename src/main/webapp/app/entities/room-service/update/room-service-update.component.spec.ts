import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RoomServiceService } from '../service/room-service.service';
import { IRoomService, RoomService } from '../room-service.model';

import { RoomServiceUpdateComponent } from './room-service-update.component';

describe('RoomService Management Update Component', () => {
  let comp: RoomServiceUpdateComponent;
  let fixture: ComponentFixture<RoomServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomServiceService: RoomServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RoomServiceUpdateComponent],
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
      .overrideTemplate(RoomServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomServiceService = TestBed.inject(RoomServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const roomService: IRoomService = { id: 456 };

      activatedRoute.data = of({ roomService });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(roomService));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomService>>();
      const roomService = { id: 123 };
      jest.spyOn(roomServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomService }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomServiceService.update).toHaveBeenCalledWith(roomService);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomService>>();
      const roomService = new RoomService();
      jest.spyOn(roomServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: roomService }));
      saveSubject.complete();

      // THEN
      expect(roomServiceService.create).toHaveBeenCalledWith(roomService);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RoomService>>();
      const roomService = { id: 123 };
      jest.spyOn(roomServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ roomService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomServiceService.update).toHaveBeenCalledWith(roomService);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
