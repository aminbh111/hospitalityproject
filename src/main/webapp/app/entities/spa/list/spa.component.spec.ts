import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SpaService } from '../service/spa.service';

import { SpaComponent } from './spa.component';

describe('Spa Management Component', () => {
  let comp: SpaComponent;
  let fixture: ComponentFixture<SpaComponent>;
  let service: SpaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SpaComponent],
    })
      .overrideTemplate(SpaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SpaService);

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
    expect(comp.spas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
