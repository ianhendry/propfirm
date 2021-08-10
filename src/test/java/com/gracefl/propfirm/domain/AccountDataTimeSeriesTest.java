package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountDataTimeSeriesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountDataTimeSeries.class);
        AccountDataTimeSeries accountDataTimeSeries1 = new AccountDataTimeSeries();
        accountDataTimeSeries1.setId(1L);
        AccountDataTimeSeries accountDataTimeSeries2 = new AccountDataTimeSeries();
        accountDataTimeSeries2.setId(accountDataTimeSeries1.getId());
        assertThat(accountDataTimeSeries1).isEqualTo(accountDataTimeSeries2);
        accountDataTimeSeries2.setId(2L);
        assertThat(accountDataTimeSeries1).isNotEqualTo(accountDataTimeSeries2);
        accountDataTimeSeries1.setId(null);
        assertThat(accountDataTimeSeries1).isNotEqualTo(accountDataTimeSeries2);
    }
}
