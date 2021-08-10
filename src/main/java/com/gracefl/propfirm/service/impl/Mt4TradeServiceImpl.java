package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.repository.Mt4TradeRepository;
import com.gracefl.propfirm.service.Mt4TradeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mt4Trade}.
 */
@Service
@Transactional
public class Mt4TradeServiceImpl implements Mt4TradeService {

    private final Logger log = LoggerFactory.getLogger(Mt4TradeServiceImpl.class);

    private final Mt4TradeRepository mt4TradeRepository;

    public Mt4TradeServiceImpl(Mt4TradeRepository mt4TradeRepository) {
        this.mt4TradeRepository = mt4TradeRepository;
    }

    @Override
    public Mt4Trade save(Mt4Trade mt4Trade) {
        log.debug("Request to save Mt4Trade : {}", mt4Trade);
        return mt4TradeRepository.save(mt4Trade);
    }

    @Override
    public Optional<Mt4Trade> partialUpdate(Mt4Trade mt4Trade) {
        log.debug("Request to partially update Mt4Trade : {}", mt4Trade);

        return mt4TradeRepository
            .findById(mt4Trade.getId())
            .map(
                existingMt4Trade -> {
                    if (mt4Trade.getTicket() != null) {
                        existingMt4Trade.setTicket(mt4Trade.getTicket());
                    }
                    if (mt4Trade.getOpenTime() != null) {
                        existingMt4Trade.setOpenTime(mt4Trade.getOpenTime());
                    }
                    if (mt4Trade.getDirectionType() != null) {
                        existingMt4Trade.setDirectionType(mt4Trade.getDirectionType());
                    }
                    if (mt4Trade.getPositionSize() != null) {
                        existingMt4Trade.setPositionSize(mt4Trade.getPositionSize());
                    }
                    if (mt4Trade.getSymbol() != null) {
                        existingMt4Trade.setSymbol(mt4Trade.getSymbol());
                    }
                    if (mt4Trade.getOpenPrice() != null) {
                        existingMt4Trade.setOpenPrice(mt4Trade.getOpenPrice());
                    }
                    if (mt4Trade.getStopLossPrice() != null) {
                        existingMt4Trade.setStopLossPrice(mt4Trade.getStopLossPrice());
                    }
                    if (mt4Trade.getTakeProfitPrice() != null) {
                        existingMt4Trade.setTakeProfitPrice(mt4Trade.getTakeProfitPrice());
                    }
                    if (mt4Trade.getCloseTime() != null) {
                        existingMt4Trade.setCloseTime(mt4Trade.getCloseTime());
                    }
                    if (mt4Trade.getClosePrice() != null) {
                        existingMt4Trade.setClosePrice(mt4Trade.getClosePrice());
                    }
                    if (mt4Trade.getCommission() != null) {
                        existingMt4Trade.setCommission(mt4Trade.getCommission());
                    }
                    if (mt4Trade.getTaxes() != null) {
                        existingMt4Trade.setTaxes(mt4Trade.getTaxes());
                    }
                    if (mt4Trade.getSwap() != null) {
                        existingMt4Trade.setSwap(mt4Trade.getSwap());
                    }
                    if (mt4Trade.getProfit() != null) {
                        existingMt4Trade.setProfit(mt4Trade.getProfit());
                    }

                    return existingMt4Trade;
                }
            )
            .map(mt4TradeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mt4Trade> findAll(Pageable pageable) {
        log.debug("Request to get all Mt4Trades");
        return mt4TradeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mt4Trade> findOne(Long id) {
        log.debug("Request to get Mt4Trade : {}", id);
        return mt4TradeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mt4Trade : {}", id);
        mt4TradeRepository.deleteById(id);
    }
}
