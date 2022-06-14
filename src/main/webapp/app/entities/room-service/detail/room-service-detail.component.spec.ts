import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RoomServiceDetailComponent } from './room-service-detail.component';

describe('RoomService Management Detail Component', () => {
  let comp: RoomServiceDetailComponent;
  let fixture: ComponentFixture<RoomServiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoomServiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ roomService: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RoomServiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RoomServiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load roomService on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.roomService).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
