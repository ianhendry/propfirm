package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.DailyAnalysisPost;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DailyAnalysisPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyAnalysisPostRepository extends JpaRepository<DailyAnalysisPost, Long>, JpaSpecificationExecutor<DailyAnalysisPost> {
    @Query("select dailyAnalysisPost from DailyAnalysisPost dailyAnalysisPost where dailyAnalysisPost.user.login = ?#{principal.username}")
    List<DailyAnalysisPost> findByUserIsCurrentUser();
}
