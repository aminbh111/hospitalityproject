import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SpaDataService } from '../service/spa-data.service';

import { SpaDataComponent } from './spa-data.component';

describe('SpaData Management Component', () => {
  let comp: SpaDataComponent;
  let fixture: ComponentFixture<SpaDataComponent>;
  let service: SpaDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SpaDataComponent],
    })
      .overrideTemplate(SpaDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpaDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SpaDataService);

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
    expect(comp.spaData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
