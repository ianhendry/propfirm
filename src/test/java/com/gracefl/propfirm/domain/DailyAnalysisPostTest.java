package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DailyAnalysisPostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyAnalysisPost.class);
        DailyAnalysisPost dailyAnalysisPost1 = new DailyAnalysisPost();
        dailyAnalysisPost1.setId(1L);
        DailyAnalysisPost dailyAnalysisPost2 = new DailyAnalysisPost();
        dailyAnalysisPost2.setId(dailyAnalysisPost1.getId());
        assertThat(dailyAnalysisPost1).isEqualTo(dailyAnalysisPost2);
        dailyAnalysisPost2.setId(2L);
        assertThat(dailyAnalysisPost1).isNotEqualTo(dailyAnalysisPost2);
        dailyAnalysisPost1.setId(null);
        assertThat(dailyAnalysisPost1).isNotEqualTo(dailyAnalysisPost2);
    }
}
