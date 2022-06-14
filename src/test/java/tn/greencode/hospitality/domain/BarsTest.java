package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class BarsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bars.class);
        Bars bars1 = new Bars();
        bars1.setId(1L);
        Bars bars2 = new Bars();
        bars2.setId(bars1.getId());
        assertThat(bars1).isEqualTo(bars2);
        bars2.setId(2L);
        assertThat(bars1).isNotEqualTo(bars2);
        bars1.setId(null);
        assertThat(bars1).isNotEqualTo(bars2);
    }
}
