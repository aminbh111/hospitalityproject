import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContactUsDataService } from '../service/contact-us-data.service';
import { IContactUsData, ContactUsData } from '../contact-us-data.model';
import { IContactUs } from 'app/entities/contact-us/contact-us.model';
import { ContactUsService } from 'app/entities/contact-us/service/contact-us.service';

import { ContactUsDataUpdateComponent } from './contact-us-data-update.component';

describe('ContactUsData Management Update Component', () => {
  let comp: ContactUsDataUpdateComponent;
  let fixture: ComponentFixture<ContactUsDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactUsDataService: ContactUsDataService;
  let contactUsService: ContactUsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContactUsDataUpdateComponent],
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
      .overrideTemplate(ContactUsDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUsDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactUsDataService = TestBed.inject(ContactUsDataService);
    contactUsService = TestBed.inject(ContactUsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ContactUs query and add missing value', () => {
      const contactUsData: IContactUsData = { id: 456 };
      const contactUs: IContactUs = { id: 29458 };
      contactUsData.contactUs = contactUs;

      const contactUsCollection: IContactUs[] = [{ id: 19416 }];
      jest.spyOn(contactUsService, 'query').mockReturnValue(of(new HttpResponse({ body: contactUsCollection })));
      const additionalContactuses = [contactUs];
      const expectedCollection: IContactUs[] = [...additionalContactuses, ...contactUsCollection];
      jest.spyOn(contactUsService, 'addContactUsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contactUsData });
      comp.ngOnInit();

      expect(contactUsService.query).toHaveBeenCalled();
      expect(contactUsService.addContactUsToCollectionIfMissing).toHaveBeenCalledWith(contactUsCollection, ...additionalContactuses);
      expect(comp.contactusesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contactUsData: IContactUsData = { id: 456 };
      const contactUs: IContactUs = { id: 1006 };
      contactUsData.contactUs = contactUs;

      activatedRoute.data = of({ contactUsData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contactUsData));
      expect(comp.contactusesSharedCollection).toContain(contactUs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUsData>>();
      const contactUsData = { id: 123 };
      jest.spyOn(contactUsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUsData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactUsDataService.update).toHaveBeenCalledWith(contactUsData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUsData>>();
      const contactUsData = new ContactUsData();
      jest.spyOn(contactUsDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUsData }));
      saveSubject.complete();

      // THEN
      expect(contactUsDataService.create).toHaveBeenCalledWith(contactUsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUsData>>();
      const contactUsData = { id: 123 };
      jest.spyOn(contactUsDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUsData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactUsDataService.update).toHaveBeenCalledWith(contactUsData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackContactUsById', () => {
      it('Should return tracked ContactUs primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContactUsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
