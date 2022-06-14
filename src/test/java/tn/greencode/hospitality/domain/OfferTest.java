package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class OfferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offer.class);
        Offer offer1 = new Offer();
        offer1.setId(1L);
        Offer offer2 = new Offer();
        offer2.setId(offer1.getId());
        assertThat(offer1).isEqualTo(offer2);
        offer2.setId(2L);
        assertThat(offer1).isNotEqualTo(offer2);
        offer1.setId(null);
        assertThat(offer1).isNotEqualTo(offer2);
    }
}
