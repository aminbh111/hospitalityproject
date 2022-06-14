import { IRoomService } from 'app/entities/room-service/room-service.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IRoomServiceData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  roomService?: IRoomService | null;
}

export class RoomServiceData implements IRoomServiceData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public roomService?: IRoomService | null
  ) {}
}

export function getRoomServiceDataIdentifier(roomServiceData: IRoomServiceData): number | undefined {
  return roomServiceData.id;
}
