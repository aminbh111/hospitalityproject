import dayjs from 'dayjs/esm';
import { Language } from 'app/entities/enumerations/language.model';

export interface IPlaylist {
  id?: number;
  date?: dayjs.Dayjs | null;
  lang?: Language | null;
  file?: string | null;
}

export class Playlist implements IPlaylist {
  constructor(public id?: number, public date?: dayjs.Dayjs | null, public lang?: Language | null, public file?: string | null) {}
}

export function getPlaylistIdentifier(playlist: IPlaylist): number | undefined {
  return playlist.id;
}
