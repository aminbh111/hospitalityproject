package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class AboutUsDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutUsData.class);
        AboutUsData aboutUsData1 = new AboutUsData();
        aboutUsData1.setId(1L);
        AboutUsData aboutUsData2 = new AboutUsData();
        aboutUsData2.setId(aboutUsData1.getId());
        assertThat(aboutUsData1).isEqualTo(aboutUsData2);
        aboutUsData2.setId(2L);
        assertThat(aboutUsData1).isNotEqualTo(aboutUsData2);
        aboutUsData1.setId(null);
        assertThat(aboutUsData1).isNotEqualTo(aboutUsData2);
    }
}
