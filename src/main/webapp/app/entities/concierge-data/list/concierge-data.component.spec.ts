import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ConciergeDataService } from '../service/concierge-data.service';

import { ConciergeDataComponent } from './concierge-data.component';

describe('ConciergeData Management Component', () => {
  let comp: ConciergeDataComponent;
  let fixture: ComponentFixture<ConciergeDataComponent>;
  let service: ConciergeDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConciergeDataComponent],
    })
      .overrideTemplate(ConciergeDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConciergeDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConciergeDataService);

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
    expect(comp.conciergeData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
