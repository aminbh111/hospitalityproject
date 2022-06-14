import dayjs from 'dayjs/esm';
import { IConciergeData } from 'app/entities/concierge-data/concierge-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IConcierge {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  conciergeData?: IConciergeData[] | null;
}

export class Concierge implements IConcierge {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public conciergeData?: IConciergeData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getConciergeIdentifier(concierge: IConcierge): number | undefined {
  return concierge.id;
}
