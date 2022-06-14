package tn.greencode.hospitality.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import tn.greencode.hospitality.web.rest.TestUtil;

class ServiceDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceData.class);
        ServiceData serviceData1 = new ServiceData();
        serviceData1.setId(1L);
        ServiceData serviceData2 = new ServiceData();
        serviceData2.setId(serviceData1.getId());
        assertThat(serviceData1).isEqualTo(serviceData2);
        serviceData2.setId(2L);
        assertThat(serviceData1).isNotEqualTo(serviceData2);
        serviceData1.setId(null);
        assertThat(serviceData1).isNotEqualTo(serviceData2);
    }
}
