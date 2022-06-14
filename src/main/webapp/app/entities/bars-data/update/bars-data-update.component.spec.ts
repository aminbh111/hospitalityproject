import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BarsDataService } from '../service/bars-data.service';
import { IBarsData, BarsData } from '../bars-data.model';
import { IBars } from 'app/entities/bars/bars.model';
import { BarsService } from 'app/entities/bars/service/bars.service';

import { BarsDataUpdateComponent } from './bars-data-update.component';

describe('BarsData Management Update Component', () => {
  let comp: BarsDataUpdateComponent;
  let fixture: ComponentFixture<BarsDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let barsDataService: BarsDataService;
  let barsService: BarsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BarsDataUpdateComponent],
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
      .overrideTemplate(BarsDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BarsDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    barsDataService = TestBed.inject(BarsDataService);
    barsService = TestBed.inject(BarsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bars query and add missing value', () => {
      const barsData: IBarsData = { id: 456 };
      const bars: IBars = { id: 24643 };
      barsData.bars = bars;

      const barsCollection: IBars[] = [{ id: 30300 }];
      jest.spyOn(barsService, 'query').mockReturnValue(of(new HttpResponse({ body: barsCollection })));
      const additionalBars = [bars];
      const expectedCollection: IBars[] = [...additionalBars, ...barsCollection];
      jest.spyOn(barsService, 'addBarsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ barsData });
      comp.ngOnInit();

      expect(barsService.query).toHaveBeenCalled();
      expect(barsService.addBarsToCollectionIfMissing).toHaveBeenCalledWith(barsCollection, ...additionalBars);
      expect(comp.barsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const barsData: IBarsData = { id: 456 };
      const bars: IBars = { id: 86576 };
      barsData.bars = bars;

      activatedRoute.data = of({ barsData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(barsData));
      expect(comp.barsSharedCollection).toContain(bars);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BarsData>>();
      const barsData = { id: 123 };
      jest.spyOn(barsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ barsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: barsData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(barsDataService.update).toHaveBeenCalledWith(barsData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BarsData>>();
      const barsData = new BarsData();
      jest.spyOn(barsDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ barsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: barsData }));
      saveSubject.complete();

      // THEN
      expect(barsDataService.create).toHaveBeenCalledWith(barsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BarsData>>();
      const barsData = { id: 123 };
      jest.spyOn(barsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ barsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(barsDataService.update).toHaveBeenCalledWith(barsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBarsById', () => {
      it('Should return tracked Bars primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBarsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
