import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConciergeService } from '../service/concierge.service';
import { IConcierge, Concierge } from '../concierge.model';

import { ConciergeUpdateComponent } from './concierge-update.component';

describe('Concierge Management Update Component', () => {
  let comp: ConciergeUpdateComponent;
  let fixture: ComponentFixture<ConciergeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conciergeService: ConciergeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConciergeUpdateComponent],
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
      .overrideTemplate(ConciergeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConciergeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conciergeService = TestBed.inject(ConciergeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const concierge: IConcierge = { id: 456 };

      activatedRoute.data = of({ concierge });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(concierge));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Concierge>>();
      const concierge = { id: 123 };
      jest.spyOn(conciergeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concierge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concierge }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(conciergeService.update).toHaveBeenCalledWith(concierge);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Concierge>>();
      const concierge = new Concierge();
      jest.spyOn(conciergeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concierge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: concierge }));
      saveSubject.complete();

      // THEN
      expect(conciergeService.create).toHaveBeenCalledWith(concierge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Concierge>>();
      const concierge = { id: 123 };
      jest.spyOn(conciergeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ concierge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conciergeService.update).toHaveBeenCalledWith(concierge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
