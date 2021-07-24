package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.repository.InstrumentRepository;
import com.gracefl.propfirm.service.InstrumentService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Instrument}.
 */
@Service
@Transactional
public class InstrumentServiceImpl implements InstrumentService {

    private final Logger log = LoggerFactory.getLogger(InstrumentServiceImpl.class);

    private final InstrumentRepository instrumentRepository;

    public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public Instrument save(Instrument instrument) {
        log.debug("Request to save Instrument : {}", instrument);
        return instrumentRepository.save(instrument);
    }

    @Override
    public Optional<Instrument> partialUpdate(Instrument instrument) {
        log.debug("Request to partially update Instrument : {}", instrument);

        return instrumentRepository
            .findById(instrument.getId())
            .map(
                existingInstrument -> {
                    if (instrument.getTicker() != null) {
                        existingInstrument.setTicker(instrument.getTicker());
                    }
                    if (instrument.getInstrumentType() != null) {
                        existingInstrument.setInstrumentType(instrument.getInstrumentType());
                    }
                    if (instrument.getExchange() != null) {
                        existingInstrument.setExchange(instrument.getExchange());
                    }
                    if (instrument.getAverageSpread() != null) {
                        existingInstrument.setAverageSpread(instrument.getAverageSpread());
                    }
                    if (instrument.getTradeRestrictions() != null) {
                        existingInstrument.setTradeRestrictions(instrument.getTradeRestrictions());
                    }
                    if (instrument.getInActive() != null) {
                        existingInstrument.setInActive(instrument.getInActive());
                    }
                    if (instrument.getInActiveDate() != null) {
                        existingInstrument.setInActiveDate(instrument.getInActiveDate());
                    }

                    return existingInstrument;
                }
            )
            .map(instrumentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Instrument> findAll() {
        log.debug("Request to get all Instruments");
        return instrumentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Instrument> findOne(Long id) {
        log.debug("Request to get Instrument : {}", id);
        return instrumentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Instrument : {}", id);
        instrumentRepository.deleteById(id);
    }

	@Override
	public List<Instrument> findAllByTicker(String ticker) {
		log.debug("Request to get all Instrument where ticker : {} ", ticker);
        return StreamSupport
            .stream(instrumentRepository.findAll().spliterator(), false)
            .filter(mt4Account -> mt4Account.getTicker() == ticker)
            .collect(Collectors.toList());
	}
    
    
}
