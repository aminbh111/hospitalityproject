package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class BarsDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarsData.class);
        BarsData barsData1 = new BarsData();
        barsData1.setId(1L);
        BarsData barsData2 = new BarsData();
        barsData2.setId(barsData1.getId());
        assertThat(barsData1).isEqualTo(barsData2);
        barsData2.setId(2L);
        assertThat(barsData1).isNotEqualTo(barsData2);
        barsData1.setId(null);
        assertThat(barsData1).isNotEqualTo(barsData2);
    }
}
