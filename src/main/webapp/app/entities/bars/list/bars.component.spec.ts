import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BarsService } from '../service/bars.service';

import { BarsComponent } from './bars.component';

describe('Bars Management Component', () => {
  let comp: BarsComponent;
  let fixture: ComponentFixture<BarsComponent>;
  let service: BarsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BarsComponent],
    })
      .overrideTemplate(BarsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BarsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BarsService);

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
    expect(comp.bars?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
