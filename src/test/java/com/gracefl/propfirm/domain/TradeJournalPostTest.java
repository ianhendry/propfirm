package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TradeJournalPostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TradeJournalPost.class);
        TradeJournalPost tradeJournalPost1 = new TradeJournalPost();
        tradeJournalPost1.setId(1L);
        TradeJournalPost tradeJournalPost2 = new TradeJournalPost();
        tradeJournalPost2.setId(tradeJournalPost1.getId());
        assertThat(tradeJournalPost1).isEqualTo(tradeJournalPost2);
        tradeJournalPost2.setId(2L);
        assertThat(tradeJournalPost1).isNotEqualTo(tradeJournalPost2);
        tradeJournalPost1.setId(null);
        assertThat(tradeJournalPost1).isNotEqualTo(tradeJournalPost2);
    }
}
