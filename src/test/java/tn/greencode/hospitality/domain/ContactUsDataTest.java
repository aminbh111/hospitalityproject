package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class ContactUsDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactUsData.class);
        ContactUsData contactUsData1 = new ContactUsData();
        contactUsData1.setId(1L);
        ContactUsData contactUsData2 = new ContactUsData();
        contactUsData2.setId(contactUsData1.getId());
        assertThat(contactUsData1).isEqualTo(contactUsData2);
        contactUsData2.setId(2L);
        assertThat(contactUsData1).isNotEqualTo(contactUsData2);
        contactUsData1.setId(null);
        assertThat(contactUsData1).isNotEqualTo(contactUsData2);
    }
}
