import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MeetingService } from '../service/meeting.service';
import { IMeeting, Meeting } from '../meeting.model';

import { MeetingUpdateComponent } from './meeting-update.component';

describe('Meeting Management Update Component', () => {
  let comp: MeetingUpdateComponent;
  let fixture: ComponentFixture<MeetingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let meetingService: MeetingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MeetingUpdateComponent],
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
      .overrideTemplate(MeetingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeetingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    meetingService = TestBed.inject(MeetingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const meeting: IMeeting = { id: 456 };

      activatedRoute.data = of({ meeting });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(meeting));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Meeting>>();
      const meeting = { id: 123 };
      jest.spyOn(meetingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meeting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meeting }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(meetingService.update).toHaveBeenCalledWith(meeting);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Meeting>>();
      const meeting = new Meeting();
      jest.spyOn(meetingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meeting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: meeting }));
      saveSubject.complete();

      // THEN
      expect(meetingService.create).toHaveBeenCalledWith(meeting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Meeting>>();
      const meeting = { id: 123 };
      jest.spyOn(meetingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ meeting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(meetingService.update).toHaveBeenCalledWith(meeting);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
