import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpaService } from '../service/spa.service';
import { ISpa, Spa } from '../spa.model';

import { SpaUpdateComponent } from './spa-update.component';

describe('Spa Management Update Component', () => {
  let comp: SpaUpdateComponent;
  let fixture: ComponentFixture<SpaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let spaService: SpaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpaUpdateComponent],
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
      .overrideTemplate(SpaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    spaService = TestBed.inject(SpaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const spa: ISpa = { id: 456 };

      activatedRoute.data = of({ spa });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(spa));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Spa>>();
      const spa = { id: 123 };
      jest.spyOn(spaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spa }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(spaService.update).toHaveBeenCalledWith(spa);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Spa>>();
      const spa = new Spa();
      jest.spyOn(spaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: spa }));
      saveSubject.complete();

      // THEN
      expect(spaService.create).toHaveBeenCalledWith(spa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Spa>>();
      const spa = { id: 123 };
      jest.spyOn(spaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ spa });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(spaService.update).toHaveBeenCalledWith(spa);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
