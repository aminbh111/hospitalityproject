import { IConcierge } from 'app/entities/concierge/concierge.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IConciergeData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  concierge?: IConcierge | null;
}

export class ConciergeData implements IConciergeData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public concierge?: IConcierge | null
  ) {}
}

export function getConciergeDataIdentifier(conciergeData: IConciergeData): number | undefined {
  return conciergeData.id;
}
