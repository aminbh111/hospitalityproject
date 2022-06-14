import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlaylistService } from '../service/playlist.service';
import { IPlaylist, Playlist } from '../playlist.model';

import { PlaylistUpdateComponent } from './playlist-update.component';

describe('Playlist Management Update Component', () => {
  let comp: PlaylistUpdateComponent;
  let fixture: ComponentFixture<PlaylistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playlistService: PlaylistService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlaylistUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PlaylistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaylistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playlistService = TestBed.inject(PlaylistService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const playlist: IPlaylist = { id: 456 };

      activatedRoute.data = of({ playlist });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(playlist));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Playlist>>();
      const playlist = { id: 123 };
      jest.spyOn(playlistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playlist }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(playlistService.update).toHaveBeenCalledWith(playlist);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Playlist>>();
      const playlist = new Playlist();
      jest.spyOn(playlistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playlist }));
      saveSubject.complete();

      // THEN
      expect(playlistService.create).toHaveBeenCalledWith(playlist);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Playlist>>();
      const playlist = { id: 123 };
      jest.spyOn(playlistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playlist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playlistService.update).toHaveBeenCalledWith(playlist);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
