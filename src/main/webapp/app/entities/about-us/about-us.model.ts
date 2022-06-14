import dayjs from 'dayjs/esm';
import { IAboutUsData } from 'app/entities/about-us-data/about-us-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IAboutUs {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  aboutUsData?: IAboutUsData[] | null;
}

export class AboutUs implements IAboutUs {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public aboutUsData?: IAboutUsData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getAboutUsIdentifier(aboutUs: IAboutUs): number | undefined {
  return aboutUs.id;
}
