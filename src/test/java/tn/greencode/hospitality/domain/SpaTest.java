package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class SpaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spa.class);
        Spa spa1 = new Spa();
        spa1.setId(1L);
        Spa spa2 = new Spa();
        spa2.setId(spa1.getId());
        assertThat(spa1).isEqualTo(spa2);
        spa2.setId(2L);
        assertThat(spa1).isNotEqualTo(spa2);
        spa1.setId(null);
        assertThat(spa1).isNotEqualTo(spa2);
    }
}
