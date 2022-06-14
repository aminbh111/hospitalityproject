package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class AboutUsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AboutUs.class);
        AboutUs aboutUs1 = new AboutUs();
        aboutUs1.setId(1L);
        AboutUs aboutUs2 = new AboutUs();
        aboutUs2.setId(aboutUs1.getId());
        assertThat(aboutUs1).isEqualTo(aboutUs2);
        aboutUs2.setId(2L);
        assertThat(aboutUs1).isNotEqualTo(aboutUs2);
        aboutUs1.setId(null);
        assertThat(aboutUs1).isNotEqualTo(aboutUs2);
    }
}
