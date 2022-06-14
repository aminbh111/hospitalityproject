import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RoomServiceDataService } from '../service/room-service-data.service';

import { RoomServiceDataComponent } from './room-service-data.component';

describe('RoomServiceData Management Component', () => {
  let comp: RoomServiceDataComponent;
  let fixture: ComponentFixture<RoomServiceDataComponent>;
  let service: RoomServiceDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RoomServiceDataComponent],
    })
      .overrideTemplate(RoomServiceDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomServiceDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RoomServiceDataService);

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
    expect(comp.roomServiceData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
