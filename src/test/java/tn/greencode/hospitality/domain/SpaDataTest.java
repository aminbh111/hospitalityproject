package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class SpaDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpaData.class);
        SpaData spaData1 = new SpaData();
        spaData1.setId(1L);
        SpaData spaData2 = new SpaData();
        spaData2.setId(spaData1.getId());
        assertThat(spaData1).isEqualTo(spaData2);
        spaData2.setId(2L);
        assertThat(spaData1).isNotEqualTo(spaData2);
        spaData1.setId(null);
        assertThat(spaData1).isNotEqualTo(spaData2);
    }
}
