import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AboutUsDetailComponent } from './about-us-detail.component';

describe('AboutUs Management Detail Component', () => {
  let comp: AboutUsDetailComponent;
  let fixture: ComponentFixture<AboutUsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AboutUsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aboutUs: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AboutUsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AboutUsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aboutUs on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aboutUs).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
