package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class RoomServiceDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomServiceData.class);
        RoomServiceData roomServiceData1 = new RoomServiceData();
        roomServiceData1.setId(1L);
        RoomServiceData roomServiceData2 = new RoomServiceData();
        roomServiceData2.setId(roomServiceData1.getId());
        assertThat(roomServiceData1).isEqualTo(roomServiceData2);
        roomServiceData2.setId(2L);
        assertThat(roomServiceData1).isNotEqualTo(roomServiceData2);
        roomServiceData1.setId(null);
        assertThat(roomServiceData1).isNotEqualTo(roomServiceData2);
    }
}
