import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RoomServiceService } from '../service/room-service.service';

import { RoomServiceComponent } from './room-service.component';

describe('RoomService Management Component', () => {
  let comp: RoomServiceComponent;
  let fixture: ComponentFixture<RoomServiceComponent>;
  let service: RoomServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RoomServiceComponent],
    })
      .overrideTemplate(RoomServiceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomServiceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RoomServiceService);

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
    expect(comp.roomServices?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
