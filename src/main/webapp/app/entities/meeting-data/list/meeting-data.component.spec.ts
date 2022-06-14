import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MeetingDataService } from '../service/meeting-data.service';

import { MeetingDataComponent } from './meeting-data.component';

describe('MeetingData Management Component', () => {
  let comp: MeetingDataComponent;
  let fixture: ComponentFixture<MeetingDataComponent>;
  let service: MeetingDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeetingDataComponent],
    })
      .overrideTemplate(MeetingDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeetingDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MeetingDataService);

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
    expect(comp.meetingData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
