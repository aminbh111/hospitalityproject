import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AboutUsDataService } from '../service/about-us-data.service';
import { IAboutUsData, AboutUsData } from '../about-us-data.model';
import { IAboutUs } from 'app/entities/about-us/about-us.model';
import { AboutUsService } from 'app/entities/about-us/service/about-us.service';

import { AboutUsDataUpdateComponent } from './about-us-data-update.component';

describe('AboutUsData Management Update Component', () => {
  let comp: AboutUsDataUpdateComponent;
  let fixture: ComponentFixture<AboutUsDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aboutUsDataService: AboutUsDataService;
  let aboutUsService: AboutUsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AboutUsDataUpdateComponent],
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
      .overrideTemplate(AboutUsDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AboutUsDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aboutUsDataService = TestBed.inject(AboutUsDataService);
    aboutUsService = TestBed.inject(AboutUsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AboutUs query and add missing value', () => {
      const aboutUsData: IAboutUsData = { id: 456 };
      const aboutUs: IAboutUs = { id: 58505 };
      aboutUsData.aboutUs = aboutUs;

      const aboutUsCollection: IAboutUs[] = [{ id: 64028 }];
      jest.spyOn(aboutUsService, 'query').mockReturnValue(of(new HttpResponse({ body: aboutUsCollection })));
      const additionalAboutuses = [aboutUs];
      const expectedCollection: IAboutUs[] = [...additionalAboutuses, ...aboutUsCollection];
      jest.spyOn(aboutUsService, 'addAboutUsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aboutUsData });
      comp.ngOnInit();

      expect(aboutUsService.query).toHaveBeenCalled();
      expect(aboutUsService.addAboutUsToCollectionIfMissing).toHaveBeenCalledWith(aboutUsCollection, ...additionalAboutuses);
      expect(comp.aboutusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aboutUsData: IAboutUsData = { id: 456 };
      const aboutUs: IAboutUs = { id: 93934 };
      aboutUsData.aboutUs = aboutUs;

      activatedRoute.data = of({ aboutUsData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aboutUsData));
      expect(comp.aboutusesSharedCollection).toContain(aboutUs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AboutUsData>>();
      const aboutUsData = { id: 123 };
      jest.spyOn(aboutUsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutUsData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aboutUsDataService.update).toHaveBeenCalledWith(aboutUsData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AboutUsData>>();
      const aboutUsData = new AboutUsData();
      jest.spyOn(aboutUsDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aboutUsData }));
      saveSubject.complete();

      // THEN
      expect(aboutUsDataService.create).toHaveBeenCalledWith(aboutUsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AboutUsData>>();
      const aboutUsData = { id: 123 };
      jest.spyOn(aboutUsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aboutUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aboutUsDataService.update).toHaveBeenCalledWith(aboutUsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAboutUsById', () => {
      it('Should return tracked AboutUs primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAboutUsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
