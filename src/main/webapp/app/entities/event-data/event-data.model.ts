import { IEvent } from 'app/entities/event/event.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IEventData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  event?: IEvent | null;
}

export class EventData implements IEventData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public event?: IEvent | null
  ) {}
}

export function getEventDataIdentifier(eventData: IEventData): number | undefined {
  return eventData.id;
}
