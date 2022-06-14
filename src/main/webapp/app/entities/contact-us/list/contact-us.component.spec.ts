import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContactUsService } from '../service/contact-us.service';

import { ContactUsComponent } from './contact-us.component';

describe('ContactUs Management Component', () => {
  let comp: ContactUsComponent;
  let fixture: ComponentFixture<ContactUsComponent>;
  let service: ContactUsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContactUsComponent],
    })
      .overrideTemplate(ContactUsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContactUsService);

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
    expect(comp.contactuses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
