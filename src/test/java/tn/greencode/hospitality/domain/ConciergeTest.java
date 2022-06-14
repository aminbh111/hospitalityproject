package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class ConciergeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Concierge.class);
        Concierge concierge1 = new Concierge();
        concierge1.setId(1L);
        Concierge concierge2 = new Concierge();
        concierge2.setId(concierge1.getId());
        assertThat(concierge1).isEqualTo(concierge2);
        concierge2.setId(2L);
        assertThat(concierge1).isNotEqualTo(concierge2);
        concierge1.setId(null);
        assertThat(concierge1).isNotEqualTo(concierge2);
    }
}
