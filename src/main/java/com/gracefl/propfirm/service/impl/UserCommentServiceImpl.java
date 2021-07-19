package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.UserComment;
import com.gracefl.propfirm.repository.UserCommentRepository;
import com.gracefl.propfirm.service.UserCommentService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserComment}.
 */
@Service
@Transactional
public class UserCommentServiceImpl implements UserCommentService {

    private final Logger log = LoggerFactory.getLogger(UserCommentServiceImpl.class);

    private final UserCommentRepository userCommentRepository;

    public UserCommentServiceImpl(UserCommentRepository userCommentRepository) {
        this.userCommentRepository = userCommentRepository;
    }

    @Override
    public UserComment save(UserComment userComment) {
        log.debug("Request to save UserComment : {}", userComment);
        return userCommentRepository.save(userComment);
    }

    @Override
    public Optional<UserComment> partialUpdate(UserComment userComment) {
        log.debug("Request to partially update UserComment : {}", userComment);

        return userCommentRepository
            .findById(userComment.getId())
            .map(
                existingUserComment -> {
                    if (userComment.getCommentTitle() != null) {
                        existingUserComment.setCommentTitle(userComment.getCommentTitle());
                    }
                    if (userComment.getCommentBody() != null) {
                        existingUserComment.setCommentBody(userComment.getCommentBody());
                    }
                    if (userComment.getCommentMedia() != null) {
                        existingUserComment.setCommentMedia(userComment.getCommentMedia());
                    }
                    if (userComment.getCommentMediaContentType() != null) {
                        existingUserComment.setCommentMediaContentType(userComment.getCommentMediaContentType());
                    }
                    if (userComment.getDateAdded() != null) {
                        existingUserComment.setDateAdded(userComment.getDateAdded());
                    }
                    if (userComment.getMakePublicVisibleOnSite() != null) {
                        existingUserComment.setMakePublicVisibleOnSite(userComment.getMakePublicVisibleOnSite());
                    }

                    return existingUserComment;
                }
            )
            .map(userCommentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserComment> findAll(Pageable pageable) {
        log.debug("Request to get all UserComments");
        return userCommentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserComment> findOne(Long id) {
        log.debug("Request to get UserComment : {}", id);
        return userCommentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserComment : {}", id);
        userCommentRepository.deleteById(id);
    }
}
