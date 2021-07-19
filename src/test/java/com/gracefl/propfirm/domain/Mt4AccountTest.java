package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class Mt4AccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mt4Account.class);
        Mt4Account mt4Account1 = new Mt4Account();
        mt4Account1.setId(1L);
        Mt4Account mt4Account2 = new Mt4Account();
        mt4Account2.setId(mt4Account1.getId());
        assertThat(mt4Account1).isEqualTo(mt4Account2);
        mt4Account2.setId(2L);
        assertThat(mt4Account1).isNotEqualTo(mt4Account2);
        mt4Account1.setId(null);
        assertThat(mt4Account1).isNotEqualTo(mt4Account2);
    }
}
