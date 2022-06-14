import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServiceDataService } from '../service/service-data.service';
import { IServiceData, ServiceData } from '../service-data.model';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';

import { ServiceDataUpdateComponent } from './service-data-update.component';

describe('ServiceData Management Update Component', () => {
  let comp: ServiceDataUpdateComponent;
  let fixture: ComponentFixture<ServiceDataUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceDataService: ServiceDataService;
  let serviceService: ServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ServiceDataUpdateComponent],
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
      .overrideTemplate(ServiceDataUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceDataUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceDataService = TestBed.inject(ServiceDataService);
    serviceService = TestBed.inject(ServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Service query and add missing value', () => {
      const serviceData: IServiceData = { id: 456 };
      const service: IService = { id: 19877 };
      serviceData.service = service;

      const serviceCollection: IService[] = [{ id: 3874 }];
      jest.spyOn(serviceService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceCollection })));
      const additionalServices = [service];
      const expectedCollection: IService[] = [...additionalServices, ...serviceCollection];
      jest.spyOn(serviceService, 'addServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceData });
      comp.ngOnInit();

      expect(serviceService.query).toHaveBeenCalled();
      expect(serviceService.addServiceToCollectionIfMissing).toHaveBeenCalledWith(serviceCollection, ...additionalServices);
      expect(comp.servicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const serviceData: IServiceData = { id: 456 };
      const service: IService = { id: 61980 };
      serviceData.service = service;

      activatedRoute.data = of({ serviceData });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(serviceData));
      expect(comp.servicesSharedCollection).toContain(service);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceData>>();
      const serviceData = { id: 123 };
      jest.spyOn(serviceDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceData }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceDataService.update).toHaveBeenCalledWith(serviceData);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceData>>();
      const serviceData = new ServiceData();
      jest.spyOn(serviceDataService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceData }));
      saveSubject.complete();

      // THEN
      expect(serviceDataService.create).toHaveBeenCalledWith(serviceData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ServiceData>>();
      const serviceData = { id: 123 };
      jest.spyOn(serviceDataService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceData });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceDataService.update).toHaveBeenCalledWith(serviceData);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackServiceById', () => {
      it('Should return tracked Service primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
