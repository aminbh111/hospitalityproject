import dayjs from 'dayjs/esm';
import { IOfferData } from 'app/entities/offer-data/offer-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IOffer {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  offerData?: IOfferData[] | null;
}

export class Offer implements IOffer {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public offerData?: IOfferData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getOfferIdentifier(offer: IOffer): number | undefined {
  return offer.id;
}
