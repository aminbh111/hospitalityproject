package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class OfferDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OfferData.class);
        OfferData offerData1 = new OfferData();
        offerData1.setId(1L);
        OfferData offerData2 = new OfferData();
        offerData2.setId(offerData1.getId());
        assertThat(offerData1).isEqualTo(offerData2);
        offerData2.setId(2L);
        assertThat(offerData1).isNotEqualTo(offerData2);
        offerData1.setId(null);
        assertThat(offerData1).isNotEqualTo(offerData2);
    }
}
