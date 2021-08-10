package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.TradeJournalPost;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeJournalPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeJournalPostRepository extends JpaRepository<TradeJournalPost, Long>, JpaSpecificationExecutor<TradeJournalPost> {
    @Query("select tradeJournalPost from TradeJournalPost tradeJournalPost where tradeJournalPost.user.login = ?#{principal.username}")
    List<TradeJournalPost> findByUserIsCurrentUser();
}
