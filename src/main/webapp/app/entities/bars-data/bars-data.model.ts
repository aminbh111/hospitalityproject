import { IBars } from 'app/entities/bars/bars.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IBarsData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  bars?: IBars | null;
}

export class BarsData implements IBarsData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public bars?: IBars | null
  ) {}
}

export function getBarsDataIdentifier(barsData: IBarsData): number | undefined {
  return barsData.id;
}
