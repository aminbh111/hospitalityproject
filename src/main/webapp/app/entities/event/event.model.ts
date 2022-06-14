import dayjs from 'dayjs/esm';
import { IEventData } from 'app/entities/event-data/event-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IEvent {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  eventData?: IEventData[] | null;
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public eventData?: IEventData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}
