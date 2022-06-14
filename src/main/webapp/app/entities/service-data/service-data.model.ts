import { IService } from 'app/entities/service/service.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IServiceData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  service?: IService | null;
}

export class ServiceData implements IServiceData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public service?: IService | null
  ) {}
}

export function getServiceDataIdentifier(serviceData: IServiceData): number | undefined {
  return serviceData.id;
}
