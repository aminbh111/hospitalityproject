import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpaDetailComponent } from './spa-detail.component';

describe('Spa Management Detail Component', () => {
  let comp: SpaDetailComponent;
  let fixture: ComponentFixture<SpaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SpaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ spa: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SpaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SpaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load spa on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.spa).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
