import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContactUsDetailComponent } from './contact-us-detail.component';

describe('ContactUs Management Detail Component', () => {
  let comp: ContactUsDetailComponent;
  let fixture: ComponentFixture<ContactUsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContactUsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contactUs: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContactUsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContactUsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contactUs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contactUs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
