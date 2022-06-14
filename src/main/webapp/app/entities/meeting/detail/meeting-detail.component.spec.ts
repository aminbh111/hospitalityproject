import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MeetingDetailComponent } from './meeting-detail.component';

describe('Meeting Management Detail Component', () => {
  let comp: MeetingDetailComponent;
  let fixture: ComponentFixture<MeetingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeetingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ meeting: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MeetingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MeetingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load meeting on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.meeting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
