package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.UserComment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
    @Query("select userComment from UserComment userComment where userComment.user.login = ?#{principal.username}")
    List<UserComment> findByUserIsCurrentUser();
}
