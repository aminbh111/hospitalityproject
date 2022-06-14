import dayjs from 'dayjs/esm';
import { ISpaData } from 'app/entities/spa-data/spa-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface ISpa {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  spaData?: ISpaData[] | null;
}

export class Spa implements ISpa {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public spaData?: ISpaData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getSpaIdentifier(spa: ISpa): number | undefined {
  return spa.id;
}
