package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.Instrument;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Instrument}.
 */
public interface InstrumentService {
    /**
     * Save a instrument.
     *
     * @param instrument the entity to save.
     * @return the persisted entity.
     */
    Instrument save(Instrument instrument);

    /**
     * Partially updates a instrument.
     *
     * @param instrument the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Instrument> partialUpdate(Instrument instrument);

    /**
     * Get all the instruments.
     *
     * @return the list of entities.
     */
    List<Instrument> findAll();

    /**
     * Get the "id" instrument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Instrument> findOne(Long id);

    /**
     * Get the instrument by the ticker
     *
     * @param symbol the ticker of the entity.
     * @return the entity.
     */
    List<Instrument> findAllByTicker(String ticker);
    
    /**
     * Delete the "id" instrument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
