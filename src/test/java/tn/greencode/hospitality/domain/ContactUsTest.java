package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class ContactUsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUs.class);
        ContactUs contactUs1 = new ContactUs();
        contactUs1.setId(1L);
        ContactUs contactUs2 = new ContactUs();
        contactUs2.setId(contactUs1.getId());
        assertThat(contactUs1).isEqualTo(contactUs2);
        contactUs2.setId(2L);
        assertThat(contactUs1).isNotEqualTo(contactUs2);
        contactUs1.setId(null);
        assertThat(contactUs1).isNotEqualTo(contactUs2);
    }
}
