import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ConciergeDataService } from '../service/concierge-data.service';
import { IConciergeData, ConciergeData } from '../concierge-data.model';
import { IConcierge } from 'app/entities/concierge/concierge.model';
import { ConciergeService } from 'app/entities/concierge/service/concierge.service';

import { ConciergeDataUpdateComponent } from './concierge-data-update.component';

describe('ConciergeData Management Update Component', () => {
  let comp: ConciergeDataUpdateComponent;
  let fixture: ComponentFixture<ConciergeDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conciergeDataService: ConciergeDataService;
  let conciergeService: ConciergeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ConciergeDataUpdateComponent],
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
      .overrideTemplate(ConciergeDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConciergeDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conciergeDataService = TestBed.inject(ConciergeDataService);
    conciergeService = TestBed.inject(ConciergeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Concierge query and add missing value', () => {
      const conciergeData: IConciergeData = { id: 456 };
      const concierge: IConcierge = { id: 69846 };
      conciergeData.concierge = concierge;

      const conciergeCollection: IConcierge[] = [{ id: 81603 }];
      jest.spyOn(conciergeService, 'query').mockReturnValue(of(new HttpResponse({ body: conciergeCollection })));
      const additionalConcierges = [concierge];
      const expectedCollection: IConcierge[] = [...additionalConcierges, ...conciergeCollection];
      jest.spyOn(conciergeService, 'addConciergeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conciergeData });
      comp.ngOnInit();

      expect(conciergeService.query).toHaveBeenCalled();
      expect(conciergeService.addConciergeToCollectionIfMissing).toHaveBeenCalledWith(conciergeCollection, ...additionalConcierges);
      expect(comp.conciergesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conciergeData: IConciergeData = { id: 456 };
      const concierge: IConcierge = { id: 90237 };
      conciergeData.concierge = concierge;

      activatedRoute.data = of({ conciergeData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(conciergeData));
      expect(comp.conciergesSharedCollection).toContain(concierge);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConciergeData>>();
      const conciergeData = { id: 123 };
      jest.spyOn(conciergeDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conciergeData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conciergeData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(conciergeDataService.update).toHaveBeenCalledWith(conciergeData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConciergeData>>();
      const conciergeData = new ConciergeData();
      jest.spyOn(conciergeDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conciergeData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conciergeData }));
      saveSubject.complete();

      // THEN
      expect(conciergeDataService.create).toHaveBeenCalledWith(conciergeData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ConciergeData>>();
      const conciergeData = { id: 123 };
      jest.spyOn(conciergeDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conciergeData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conciergeDataService.update).toHaveBeenCalledWith(conciergeData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackConciergeById', () => {
      it('Should return tracked Concierge primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackConciergeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
