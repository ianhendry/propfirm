package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.Mt4Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Mt4Account}.
 */
public interface Mt4AccountService {
    /**
     * Save a mt4Account.
     *
     * @param mt4Account the entity to save.
     * @return the persisted entity.
     */
    Mt4Account save(Mt4Account mt4Account);

    /**
     * Partially updates a mt4Account.
     *
     * @param mt4Account the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mt4Account> partialUpdate(Mt4Account mt4Account);

    /**
     * Get all the mt4Accounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mt4Account> findAll(Pageable pageable);
    /**
     * Get all the Mt4Account where TradeChallenge is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Mt4Account> findAllWhereTradeChallengeIsNull();

    /**
     * Get the "id" mt4Account.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mt4Account> findOne(Long id);

    /**
     * Get the mt4Account.
     *
     * @param login the account login ID of the entity.
     * @return the entity.
     */
    List<Mt4Account> findAllByAccountLogin(String accountLogin);
    
    /**
     * Delete the "id" mt4Account.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
