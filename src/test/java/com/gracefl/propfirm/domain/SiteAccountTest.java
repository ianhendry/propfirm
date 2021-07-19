package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteAccount.class);
        SiteAccount siteAccount1 = new SiteAccount();
        siteAccount1.setId(1L);
        SiteAccount siteAccount2 = new SiteAccount();
        siteAccount2.setId(siteAccount1.getId());
        assertThat(siteAccount1).isEqualTo(siteAccount2);
        siteAccount2.setId(2L);
        assertThat(siteAccount1).isNotEqualTo(siteAccount2);
        siteAccount1.setId(null);
        assertThat(siteAccount1).isNotEqualTo(siteAccount2);
    }
}
