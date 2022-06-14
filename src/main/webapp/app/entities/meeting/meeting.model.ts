import dayjs from 'dayjs/esm';
import { IMeetingData } from 'app/entities/meeting-data/meeting-data.model';
import { Position } from 'app/entities/enumerations/position.model';

export interface IMeeting {
  id?: number;
  date?: dayjs.Dayjs | null;
  publish?: boolean | null;
  contentPosition?: Position | null;
  imagePosition?: Position | null;
  meetingData?: IMeetingData[] | null;
}

export class Meeting implements IMeeting {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public publish?: boolean | null,
    public contentPosition?: Position | null,
    public imagePosition?: Position | null,
    public meetingData?: IMeetingData[] | null
  ) {
    this.publish = this.publish ?? false;
  }
}

export function getMeetingIdentifier(meeting: IMeeting): number | undefined {
  return meeting.id;
}
