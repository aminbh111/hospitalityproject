package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class MeetingDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MeetingData.class);
        MeetingData meetingData1 = new MeetingData();
        meetingData1.setId(1L);
        MeetingData meetingData2 = new MeetingData();
        meetingData2.setId(meetingData1.getId());
        assertThat(meetingData1).isEqualTo(meetingData2);
        meetingData2.setId(2L);
        assertThat(meetingData1).isNotEqualTo(meetingData2);
        meetingData1.setId(null);
        assertThat(meetingData1).isNotEqualTo(meetingData2);
    }
}
