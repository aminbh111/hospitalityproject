import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BarsDataService } from '../service/bars-data.service';

import { BarsDataComponent } from './bars-data.component';

describe('BarsData Management Component', () => {
  let comp: BarsDataComponent;
  let fixture: ComponentFixture<BarsDataComponent>;
  let service: BarsDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BarsDataComponent],
    })
      .overrideTemplate(BarsDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BarsDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BarsDataService);

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
    expect(comp.barsData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
