import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConciergeDetailComponent } from './concierge-detail.component';

describe('Concierge Management Detail Component', () => {
  let comp: ConciergeDetailComponent;
  let fixture: ComponentFixture<ConciergeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConciergeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ concierge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConciergeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConciergeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load concierge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.concierge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
