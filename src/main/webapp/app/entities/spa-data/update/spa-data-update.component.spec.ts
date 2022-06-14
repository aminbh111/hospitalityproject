import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpaDataService } from '../service/spa-data.service';
import { ISpaData, SpaData } from '../spa-data.model';
import { ISpa } from 'app/entities/spa/spa.model';
import { SpaService } from 'app/entities/spa/service/spa.service';

import { SpaDataUpdateComponent } from './spa-data-update.component';

describe('SpaData Management Update Component', () => {
  let comp: SpaDataUpdateComponent;
  let fixture: ComponentFixture<SpaDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let spaDataService: SpaDataService;
  let spaService: SpaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpaDataUpdateComponent],
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
      .overrideTemplate(SpaDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpaDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    spaDataService = TestBed.inject(SpaDataService);
    spaService = TestBed.inject(SpaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Spa query and add missing value', () => {
      const spaData: ISpaData = { id: 456 };
      const spa: ISpa = { id: 5171 };
      spaData.spa = spa;

      const spaCollection: ISpa[] = [{ id: 53429 }];
      jest.spyOn(spaService, 'query').mockReturnValue(of(new HttpResponse({ body: spaCollection })));
      const additionalSpas = [spa];
      const expectedCollection: ISpa[] = [...additionalSpas, ...spaCollection];
      jest.spyOn(spaService, 'addSpaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ spaData });
      comp.ngOnInit();

      expect(spaService.query).toHaveBeenCalled();
      expect(spaService.addSpaToCollectionIfMissing).toHaveBeenCalledWith(spaCollection, ...additionalSpas);
      expect(comp.spasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const spaData: ISpaData = { id: 456 };
      const spa: ISpa = { id: 53752 };
      spaData.spa = spa;

      activatedRoute.data = of({ spaData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(spaData));
      expect(comp.spasSharedCollection).toContain(spa);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SpaData>>();
      const spaData = { id: 123 };
      jest.spyOn(spaDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spaData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spaData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(spaDataService.update).toHaveBeenCalledWith(spaData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SpaData>>();
      const spaData = new SpaData();
      jest.spyOn(spaDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spaData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spaData }));
      saveSubject.complete();

      // THEN
      expect(spaDataService.create).toHaveBeenCalledWith(spaData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SpaData>>();
      const spaData = { id: 123 };
      jest.spyOn(spaDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spaData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(spaDataService.update).toHaveBeenCalledWith(spaData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSpaById', () => {
      it('Should return tracked Spa primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSpaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
