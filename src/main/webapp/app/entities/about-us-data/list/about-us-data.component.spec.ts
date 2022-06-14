import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AboutUsDataService } from '../service/about-us-data.service';

import { AboutUsDataComponent } from './about-us-data.component';

describe('AboutUsData Management Component', () => {
  let comp: AboutUsDataComponent;
  let fixture: ComponentFixture<AboutUsDataComponent>;
  let service: AboutUsDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AboutUsDataComponent],
    })
      .overrideTemplate(AboutUsDataComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AboutUsDataComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AboutUsDataService);

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
    expect(comp.aboutUsData?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
