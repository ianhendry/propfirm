package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDetails.class);
        AddressDetails addressDetails1 = new AddressDetails();
        addressDetails1.setId(1L);
        AddressDetails addressDetails2 = new AddressDetails();
        addressDetails2.setId(addressDetails1.getId());
        assertThat(addressDetails1).isEqualTo(addressDetails2);
        addressDetails2.setId(2L);
        assertThat(addressDetails1).isNotEqualTo(addressDetails2);
        addressDetails1.setId(null);
        assertThat(addressDetails1).isNotEqualTo(addressDetails2);
    }
}
