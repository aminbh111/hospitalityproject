import dayjs from 'dayjs/esm';
import { IServiceData } from 'app/entities/service-data/service-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IService {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  serviceData?: IServiceData[] | null;
}

export class Service implements IService {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public serviceData?: IServiceData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getServiceIdentifier(service: IService): number | undefined {
  return service.id;
}
