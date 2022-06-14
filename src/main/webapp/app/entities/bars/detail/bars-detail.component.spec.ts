import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BarsDetailComponent } from './bars-detail.component';

describe('Bars Management Detail Component', () => {
  let comp: BarsDetailComponent;
  let fixture: ComponentFixture<BarsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BarsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bars: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BarsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BarsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bars on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bars).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
