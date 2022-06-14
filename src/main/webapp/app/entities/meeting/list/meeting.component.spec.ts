import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MeetingService } from '../service/meeting.service';

import { MeetingComponent } from './meeting.component';

describe('Meeting Management Component', () => {
  let comp: MeetingComponent;
  let fixture: ComponentFixture<MeetingComponent>;
  let service: MeetingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MeetingComponent],
    })
      .overrideTemplate(MeetingComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MeetingComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MeetingService);

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
    expect(comp.meetings?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
