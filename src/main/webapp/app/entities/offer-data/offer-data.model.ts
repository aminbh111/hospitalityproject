import { IOffer } from 'app/entities/offer/offer.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IOfferData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  offer?: IOffer | null;
}

export class OfferData implements IOfferData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public offer?: IOffer | null
  ) {}
}

export function getOfferDataIdentifier(offerData: IOfferData): number | undefined {
  return offerData.id;
}
