package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class ConciergeDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConciergeData.class);
        ConciergeData conciergeData1 = new ConciergeData();
        conciergeData1.setId(1L);
        ConciergeData conciergeData2 = new ConciergeData();
        conciergeData2.setId(conciergeData1.getId());
        assertThat(conciergeData1).isEqualTo(conciergeData2);
        conciergeData2.setId(2L);
        assertThat(conciergeData1).isNotEqualTo(conciergeData2);
        conciergeData1.setId(null);
        assertThat(conciergeData1).isNotEqualTo(conciergeData2);
    }
}
