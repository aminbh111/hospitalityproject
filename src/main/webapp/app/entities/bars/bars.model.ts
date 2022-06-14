import dayjs from 'dayjs/esm';
import { IBarsData } from 'app/entities/bars-data/bars-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IBars {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  barsData?: IBarsData[] | null;
}

export class Bars implements IBars {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public barsData?: IBarsData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getBarsIdentifier(bars: IBars): number | undefined {
  return bars.id;
}
