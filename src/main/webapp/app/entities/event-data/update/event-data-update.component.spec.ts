import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EventDataService } from '../service/event-data.service';
import { IEventData, EventData } from '../event-data.model';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

import { EventDataUpdateComponent } from './event-data-update.component';

describe('EventData Management Update Component', () => {
  let comp: EventDataUpdateComponent;
  let fixture: ComponentFixture<EventDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eventDataService: EventDataService;
  let eventService: EventService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EventDataUpdateComponent],
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
      .overrideTemplate(EventDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eventDataService = TestBed.inject(EventDataService);
    eventService = TestBed.inject(EventService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Event query and add missing value', () => {
      const eventData: IEventData = { id: 456 };
      const event: IEvent = { id: 96185 };
      eventData.event = event;

      const eventCollection: IEvent[] = [{ id: 41303 }];
      jest.spyOn(eventService, 'query').mockReturnValue(of(new HttpResponse({ body: eventCollection })));
      const additionalEvents = [event];
      const expectedCollection: IEvent[] = [...additionalEvents, ...eventCollection];
      jest.spyOn(eventService, 'addEventToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eventData });
      comp.ngOnInit();

      expect(eventService.query).toHaveBeenCalled();
      expect(eventService.addEventToCollectionIfMissing).toHaveBeenCalledWith(eventCollection, ...additionalEvents);
      expect(comp.eventsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eventData: IEventData = { id: 456 };
      const event: IEvent = { id: 73491 };
      eventData.event = event;

      activatedRoute.data = of({ eventData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(eventData));
      expect(comp.eventsSharedCollection).toContain(event);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventData>>();
      const eventData = { id: 123 };
      jest.spyOn(eventDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(eventDataService.update).toHaveBeenCalledWith(eventData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventData>>();
      const eventData = new EventData();
      jest.spyOn(eventDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eventData }));
      saveSubject.complete();

      // THEN
      expect(eventDataService.create).toHaveBeenCalledWith(eventData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<EventData>>();
      const eventData = { id: 123 };
      jest.spyOn(eventDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eventData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eventDataService.update).toHaveBeenCalledWith(eventData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEventById', () => {
      it('Should return tracked Event primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEventById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
