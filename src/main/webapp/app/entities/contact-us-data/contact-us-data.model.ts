import { IContactUs } from 'app/entities/contact-us/contact-us.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IContactUsData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  contactUs?: IContactUs | null;
}

export class ContactUsData implements IContactUsData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public contactUs?: IContactUs | null
  ) {}
}

export function getContactUsDataIdentifier(contactUsData: IContactUsData): number | undefined {
  return contactUsData.id;
}
