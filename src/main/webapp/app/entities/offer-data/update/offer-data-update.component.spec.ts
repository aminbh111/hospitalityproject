import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OfferDataService } from '../service/offer-data.service';
import { IOfferData, OfferData } from '../offer-data.model';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';

import { OfferDataUpdateComponent } from './offer-data-update.component';

describe('OfferData Management Update Component', () => {
  let comp: OfferDataUpdateComponent;
  let fixture: ComponentFixture<OfferDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let offerDataService: OfferDataService;
  let offerService: OfferService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OfferDataUpdateComponent],
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
      .overrideTemplate(OfferDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OfferDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    offerDataService = TestBed.inject(OfferDataService);
    offerService = TestBed.inject(OfferService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Offer query and add missing value', () => {
      const offerData: IOfferData = { id: 456 };
      const offer: IOffer = { id: 68724 };
      offerData.offer = offer;

      const offerCollection: IOffer[] = [{ id: 58136 }];
      jest.spyOn(offerService, 'query').mockReturnValue(of(new HttpResponse({ body: offerCollection })));
      const additionalOffers = [offer];
      const expectedCollection: IOffer[] = [...additionalOffers, ...offerCollection];
      jest.spyOn(offerService, 'addOfferToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ offerData });
      comp.ngOnInit();

      expect(offerService.query).toHaveBeenCalled();
      expect(offerService.addOfferToCollectionIfMissing).toHaveBeenCalledWith(offerCollection, ...additionalOffers);
      expect(comp.offersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const offerData: IOfferData = { id: 456 };
      const offer: IOffer = { id: 31595 };
      offerData.offer = offer;

      activatedRoute.data = of({ offerData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(offerData));
      expect(comp.offersSharedCollection).toContain(offer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OfferData>>();
      const offerData = { id: 123 };
      jest.spyOn(offerDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offerData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(offerDataService.update).toHaveBeenCalledWith(offerData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OfferData>>();
      const offerData = new OfferData();
      jest.spyOn(offerDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: offerData }));
      saveSubject.complete();

      // THEN
      expect(offerDataService.create).toHaveBeenCalledWith(offerData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OfferData>>();
      const offerData = { id: 123 };
      jest.spyOn(offerDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ offerData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(offerDataService.update).toHaveBeenCalledWith(offerData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOfferById', () => {
      it('Should return tracked Offer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOfferById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
