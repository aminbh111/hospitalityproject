import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ServiceDataService } from '../service/service-data.service';

import { ServiceDataComponent } from './service-data.component';

describe('ServiceData Management Component', () => {
  let comp: ServiceDataComponent;
  let fixture: ComponentFixture<ServiceDataComponent>;
  let service: ServiceDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ServiceDataComponent],
    })
      .overrideTemplate(ServiceDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ServiceDataService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.serviceData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
