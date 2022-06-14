import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContactUsService } from '../service/contact-us.service';
import { IContactUs, ContactUs } from '../contact-us.model';

import { ContactUsUpdateComponent } from './contact-us-update.component';

describe('ContactUs Management Update Component', () => {
  let comp: ContactUsUpdateComponent;
  let fixture: ComponentFixture<ContactUsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactUsService: ContactUsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContactUsUpdateComponent],
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
      .overrideTemplate(ContactUsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactUsService = TestBed.inject(ContactUsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contactUs: IContactUs = { id: 456 };

      activatedRoute.data = of({ contactUs });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(contactUs));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUs>>();
      const contactUs = { id: 123 };
      jest.spyOn(contactUsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUs }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactUsService.update).toHaveBeenCalledWith(contactUs);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUs>>();
      const contactUs = new ContactUs();
      jest.spyOn(contactUsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contactUs }));
      saveSubject.complete();

      // THEN
      expect(contactUsService.create).toHaveBeenCalledWith(contactUs);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ContactUs>>();
      const contactUs = { id: 123 };
      jest.spyOn(contactUsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contactUs });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactUsService.update).toHaveBeenCalledWith(contactUs);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
