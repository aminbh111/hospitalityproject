import { IAboutUs } from 'app/entities/about-us/about-us.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IAboutUsData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  aboutUs?: IAboutUs | null;
}

export class AboutUsData implements IAboutUsData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public aboutUs?: IAboutUs | null
  ) {}
}

export function getAboutUsDataIdentifier(aboutUsData: IAboutUsData): number | undefined {
  return aboutUsData.id;
}
