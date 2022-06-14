import dayjs from 'dayjs/esm';
import { IContactUsData } from 'app/entities/contact-us-data/contact-us-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IContactUs {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  contactUsData?: IContactUsData[] | null;
}

export class ContactUs implements IContactUs {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public contactUsData?: IContactUsData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getContactUsIdentifier(contactUs: IContactUs): number | undefined {
  return contactUs.id;
}
