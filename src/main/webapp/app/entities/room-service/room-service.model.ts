import dayjs from 'dayjs/esm';
import { IRoomServiceData } from 'app/entities/room-service-data/room-service-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IRoomService {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  roomServiceData?: IRoomServiceData[] | null;
}

export class RoomService implements IRoomService {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public roomServiceData?: IRoomServiceData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getRoomServiceIdentifier(roomService: IRoomService): number | undefined {
  return roomService.id;
}
