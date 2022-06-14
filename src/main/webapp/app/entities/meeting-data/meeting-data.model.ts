import { IMeeting } from 'app/entities/meeting/meeting.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IMeetingData {
  id?: number;
  lang?: Language | null;
  title?: string | null;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  meeting?: IMeeting | null;
}

export class MeetingData implements IMeetingData {
  constructor(
    public id?: number,
    public lang?: Language | null,
    public title?: string | null,
    public content?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public meeting?: IMeeting | null
  ) {}
}

export function getMeetingDataIdentifier(meetingData: IMeetingData): number | undefined {
  return meetingData.id;
}
