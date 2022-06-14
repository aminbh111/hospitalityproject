package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class RoomServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomService.class);
        RoomService roomService1 = new RoomService();
        roomService1.setId(1L);
        RoomService roomService2 = new RoomService();
        roomService2.setId(roomService1.getId());
        assertThat(roomService1).isEqualTo(roomService2);
        roomService2.setId(2L);
        assertThat(roomService1).isNotEqualTo(roomService2);
        roomService1.setId(null);
        assertThat(roomService1).isNotEqualTo(roomService2);
    }
}
