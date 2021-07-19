package com.gracefl.propfirm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefl.propfirm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChallengeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChallengeType.class);
        ChallengeType challengeType1 = new ChallengeType();
        challengeType1.setId(1L);
        ChallengeType challengeType2 = new ChallengeType();
        challengeType2.setId(challengeType1.getId());
        assertThat(challengeType1).isEqualTo(challengeType2);
        challengeType2.setId(2L);
        assertThat(challengeType1).isNotEqualTo(challengeType2);
        challengeType1.setId(null);
        assertThat(challengeType1).isNotEqualTo(challengeType2);
    }
}
