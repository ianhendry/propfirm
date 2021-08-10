package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TradeChallengeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TradeChallenge.class);
        TradeChallenge tradeChallenge1 = new TradeChallenge();
        tradeChallenge1.setId(1L);
        TradeChallenge tradeChallenge2 = new TradeChallenge();
        tradeChallenge2.setId(tradeChallenge1.getId());
        assertThat(tradeChallenge1).isEqualTo(tradeChallenge2);
        tradeChallenge2.setId(2L);
        assertThat(tradeChallenge1).isNotEqualTo(tradeChallenge2);
        tradeChallenge1.setId(null);
        assertThat(tradeChallenge1).isNotEqualTo(tradeChallenge2);
    }
}
