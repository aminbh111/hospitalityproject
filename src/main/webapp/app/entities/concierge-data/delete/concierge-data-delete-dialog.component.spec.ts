jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ConciergeDataService } from '../service/concierge-data.service';

import { ConciergeDataDeleteDialogComponent } from './concierge-data-delete-dialog.component';

describe('ConciergeData Management Delete Component', () => {
  let comp: ConciergeDataDeleteDialogComponent;
  let fixture: ComponentFixture<ConciergeDataDeleteDialogComponent>;
  let service: ConciergeDataService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ConciergeDataDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ConciergeDataDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConciergeDataDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConciergeDataService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
