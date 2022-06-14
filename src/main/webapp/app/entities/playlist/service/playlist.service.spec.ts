import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Language } from 'app/entities/enumerations/language.model';
import { IPlaylist, Playlist } from '../playlist.model';

import { PlaylistService } from './playlist.service';

describe('Playlist Service', () => {
  let service: PlaylistService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlaylist;
  let expectedResult: IPlaylist | IPlaylist[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlaylistService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      lang: Language.FR,
      file: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Playlist', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Playlist()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Playlist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          lang: 'BBBBBB',
          file: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Playlist', () => {
      const patchObject = Object.assign(
        {
          file: 'BBBBBB',
        },
        new Playlist()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Playlist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_TIME_FORMAT),
          lang: 'BBBBBB',
          file: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Playlist', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlaylistToCollectionIfMissing', () => {
      it('should add a Playlist to an empty array', () => {
        const playlist: IPlaylist = { id: 123 };
        expectedResult = service.addPlaylistToCollectionIfMissing([], playlist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playlist);
      });

      it('should not add a Playlist to an array that contains it', () => {
        const playlist: IPlaylist = { id: 123 };
        const playlistCollection: IPlaylist[] = [
          {
            ...playlist,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlaylistToCollectionIfMissing(playlistCollection, playlist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Playlist to an array that doesn't contain it", () => {
        const playlist: IPlaylist = { id: 123 };
        const playlistCollection: IPlaylist[] = [{ id: 456 }];
        expectedResult = service.addPlaylistToCollectionIfMissing(playlistCollection, playlist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playlist);
      });

      it('should add only unique Playlist to an array', () => {
        const playlistArray: IPlaylist[] = [{ id: 123 }, { id: 456 }, { id: 53671 }];
        const playlistCollection: IPlaylist[] = [{ id: 123 }];
        expectedResult = service.addPlaylistToCollectionIfMissing(playlistCollection, ...playlistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playlist: IPlaylist = { id: 123 };
        const playlist2: IPlaylist = { id: 456 };
        expectedResult = service.addPlaylistToCollectionIfMissing([], playlist, playlist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playlist);
        expect(expectedResult).toContain(playlist2);
      });

      it('should accept null and undefined values', () => {
        const playlist: IPlaylist = { id: 123 };
        expectedResult = service.addPlaylistToCollectionIfMissing([], null, playlist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playlist);
      });

      it('should return initial array if no Playlist is added', () => {
        const playlistCollection: IPlaylist[] = [{ id: 123 }];
        expectedResult = service.addPlaylistToCollectionIfMissing(playlistCollection, undefined, null);
        expect(expectedResult).toEqual(playlistCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
