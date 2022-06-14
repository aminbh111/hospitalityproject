import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContactUsDataService } from '../service/contact-us-data.service';

import { ContactUsDataComponent } from './contact-us-data.component';

describe('ContactUsData Management Component', () => {
  let comp: ContactUsDataComponent;
  let fixture: ComponentFixture<ContactUsDataComponent>;
  let service: ContactUsDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContactUsDataComponent],
    })
      .overrideTemplate(ContactUsDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUsDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContactUsDataService);

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
    expect(comp.contactUsData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
