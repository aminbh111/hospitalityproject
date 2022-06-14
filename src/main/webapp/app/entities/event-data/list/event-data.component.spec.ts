import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EventDataService } from '../service/event-data.service';

import { EventDataComponent } from './event-data.component';

describe('EventData Management Component', () => {
  let comp: EventDataComponent;
  let fixture: ComponentFixture<EventDataComponent>;
  let service: EventDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EventDataComponent],
    })
      .overrideTemplate(EventDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EventDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EventDataService);

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
    expect(comp.eventData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
