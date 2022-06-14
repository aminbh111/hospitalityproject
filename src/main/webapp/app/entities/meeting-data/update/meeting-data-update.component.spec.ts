import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeetingDataService } from '../service/meeting-data.service';
import { IMeetingData, MeetingData } from '../meeting-data.model';
import { IMeeting } from 'app/entities/meeting/meeting.model';
import { MeetingService } from 'app/entities/meeting/service/meeting.service';

import { MeetingDataUpdateComponent } from './meeting-data-update.component';

describe('MeetingData Management Update Component', () => {
  let comp: MeetingDataUpdateComponent;
  let fixture: ComponentFixture<MeetingDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meetingDataService: MeetingDataService;
  let meetingService: MeetingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MeetingDataUpdateComponent],
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
      .overrideTemplate(MeetingDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeetingDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meetingDataService = TestBed.inject(MeetingDataService);
    meetingService = TestBed.inject(MeetingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Meeting query and add missing value', () => {
      const meetingData: IMeetingData = { id: 456 };
      const meeting: IMeeting = { id: 37704 };
      meetingData.meeting = meeting;

      const meetingCollection: IMeeting[] = [{ id: 26480 }];
      jest.spyOn(meetingService, 'query').mockReturnValue(of(new HttpResponse({ body: meetingCollection })));
      const additionalMeetings = [meeting];
      const expectedCollection: IMeeting[] = [...additionalMeetings, ...meetingCollection];
      jest.spyOn(meetingService, 'addMeetingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ meetingData });
      comp.ngOnInit();

      expect(meetingService.query).toHaveBeenCalled();
      expect(meetingService.addMeetingToCollectionIfMissing).toHaveBeenCalledWith(meetingCollection, ...additionalMeetings);
      expect(comp.meetingsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const meetingData: IMeetingData = { id: 456 };
      const meeting: IMeeting = { id: 55446 };
      meetingData.meeting = meeting;

      activatedRoute.data = of({ meetingData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(meetingData));
      expect(comp.meetingsSharedCollection).toContain(meeting);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeetingData>>();
      const meetingData = { id: 123 };
      jest.spyOn(meetingDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meetingData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meetingData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(meetingDataService.update).toHaveBeenCalledWith(meetingData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeetingData>>();
      const meetingData = new MeetingData();
      jest.spyOn(meetingDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meetingData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meetingData }));
      saveSubject.complete();

      // THEN
      expect(meetingDataService.create).toHaveBeenCalledWith(meetingData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MeetingData>>();
      const meetingData = { id: 123 };
      jest.spyOn(meetingDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meetingData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meetingDataService.update).toHaveBeenCalledWith(meetingData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMeetingById', () => {
      it('Should return tracked Meeting primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMeetingById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
