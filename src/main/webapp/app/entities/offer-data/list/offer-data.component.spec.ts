import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OfferDataService } from '../service/offer-data.service';

import { OfferDataComponent } from './offer-data.component';

describe('OfferData Management Component', () => {
  let comp: OfferDataComponent;
  let fixture: ComponentFixture<OfferDataComponent>;
  let service: OfferDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OfferDataComponent],
    })
      .overrideTemplate(OfferDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OfferDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OfferDataService);

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
    expect(comp.offerData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
