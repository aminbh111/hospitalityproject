import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BarsService } from '../service/bars.service';
import { IBars, Bars } from '../bars.model';

import { BarsUpdateComponent } from './bars-update.component';

describe('Bars Management Update Component', () => {
  let comp: BarsUpdateComponent;
  let fixture: ComponentFixture<BarsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let barsService: BarsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BarsUpdateComponent],
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
      .overrideTemplate(BarsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BarsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    barsService = TestBed.inject(BarsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bars: IBars = { id: 456 };

      activatedRoute.data = of({ bars });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bars));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bars>>();
      const bars = { id: 123 };
      jest.spyOn(barsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bars });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bars }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(barsService.update).toHaveBeenCalledWith(bars);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bars>>();
      const bars = new Bars();
      jest.spyOn(barsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bars });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bars }));
      saveSubject.complete();

      // THEN
      expect(barsService.create).toHaveBeenCalledWith(bars);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bars>>();
      const bars = { id: 123 };
      jest.spyOn(barsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bars });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(barsService.update).toHaveBeenCalledWith(bars);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
