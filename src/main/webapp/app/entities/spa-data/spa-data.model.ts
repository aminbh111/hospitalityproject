import { ISpa } from 'app/entities/spa/spa.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface ISpaData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  spa?: ISpa | null;
}

export class SpaData implements ISpaData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public spa?: ISpa | null
  ) {}
}

export function getSpaDataIdentifier(spaData: ISpaData): number | undefined {
  return spaData.id;
}
