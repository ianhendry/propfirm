package com.gracefl.propfirm.web.rest;

import static com.gracefl.propfirm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.domain.TradeJournalPost;
import com.gracefl.propfirm.domain.enumeration.TRADEDIRECTION;
import com.gracefl.propfirm.repository.Mt4TradeRepository;
import com.gracefl.propfirm.service.criteria.Mt4TradeCriteria;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link Mt4TradeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Mt4TradeResourceIT {

    private static final BigDecimal DEFAULT_TICKET = new BigDecimal(1);
    private static final BigDecimal UPDATED_TICKET = new BigDecimal(2);
    private static final BigDecimal SMALLER_TICKET = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_OPEN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPEN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TRADEDIRECTION DEFAULT_DIRECTION_TYPE = TRADEDIRECTION.BUY;
    private static final TRADEDIRECTION UPDATED_DIRECTION_TYPE = TRADEDIRECTION.SELL;

    private static final Double DEFAULT_POSITION_SIZE = 1D;
    private static final Double UPDATED_POSITION_SIZE = 2D;
    private static final Double SMALLER_POSITION_SIZE = 1D - 1D;

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Double DEFAULT_OPEN_PRICE = 1D;
    private static final Double UPDATED_OPEN_PRICE = 2D;
    private static final Double SMALLER_OPEN_PRICE = 1D - 1D;

    private static final Double DEFAULT_STOP_LOSS_PRICE = 1D;
    private static final Double UPDATED_STOP_LOSS_PRICE = 2D;
    private static final Double SMALLER_STOP_LOSS_PRICE = 1D - 1D;

    private static final Double DEFAULT_TAKE_PROFIT_PRICE = 1D;
    private static final Double UPDATED_TAKE_PROFIT_PRICE = 2D;
    private static final Double SMALLER_TAKE_PROFIT_PRICE = 1D - 1D;

    private static final Instant DEFAULT_CLOSE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CLOSE_PRICE = 1D;
    private static final Double UPDATED_CLOSE_PRICE = 2D;
    private static final Double SMALLER_CLOSE_PRICE = 1D - 1D;

    private static final Double DEFAULT_COMMISSION = 1D;
    private static final Double UPDATED_COMMISSION = 2D;
    private static final Double SMALLER_COMMISSION = 1D - 1D;

    private static final Double DEFAULT_TAXES = 1D;
    private static final Double UPDATED_TAXES = 2D;
    private static final Double SMALLER_TAXES = 1D - 1D;

    private static final Double DEFAULT_SWAP = 1D;
    private static final Double UPDATED_SWAP = 2D;
    private static final Double SMALLER_SWAP = 1D - 1D;

    private static final Double DEFAULT_PROFIT = 1D;
    private static final Double UPDATED_PROFIT = 2D;
    private static final Double SMALLER_PROFIT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/mt-4-trades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Mt4TradeRepository mt4TradeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMt4TradeMockMvc;

    private Mt4Trade mt4Trade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mt4Trade createEntity(EntityManager em) {
        Mt4Trade mt4Trade = new Mt4Trade()
            .ticket(DEFAULT_TICKET)
            .openTime(DEFAULT_OPEN_TIME)
            .directionType(DEFAULT_DIRECTION_TYPE)
            .positionSize(DEFAULT_POSITION_SIZE)
            .symbol(DEFAULT_SYMBOL)
            .openPrice(DEFAULT_OPEN_PRICE)
            .stopLossPrice(DEFAULT_STOP_LOSS_PRICE)
            .takeProfitPrice(DEFAULT_TAKE_PROFIT_PRICE)
            .closeTime(DEFAULT_CLOSE_TIME)
            .closePrice(DEFAULT_CLOSE_PRICE)
            .commission(DEFAULT_COMMISSION)
            .taxes(DEFAULT_TAXES)
            .swap(DEFAULT_SWAP)
            .profit(DEFAULT_PROFIT);
        return mt4Trade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mt4Trade createUpdatedEntity(EntityManager em) {
        Mt4Trade mt4Trade = new Mt4Trade()
            .ticket(UPDATED_TICKET)
            .openTime(UPDATED_OPEN_TIME)
            .directionType(UPDATED_DIRECTION_TYPE)
            .positionSize(UPDATED_POSITION_SIZE)
            .symbol(UPDATED_SYMBOL)
            .openPrice(UPDATED_OPEN_PRICE)
            .stopLossPrice(UPDATED_STOP_LOSS_PRICE)
            .takeProfitPrice(UPDATED_TAKE_PROFIT_PRICE)
            .closeTime(UPDATED_CLOSE_TIME)
            .closePrice(UPDATED_CLOSE_PRICE)
            .commission(UPDATED_COMMISSION)
            .taxes(UPDATED_TAXES)
            .swap(UPDATED_SWAP)
            .profit(UPDATED_PROFIT);
        return mt4Trade;
    }

    @BeforeEach
    public void initTest() {
        mt4Trade = createEntity(em);
    }

    @Test
    @Transactional
    void createMt4Trade() throws Exception {
        int databaseSizeBeforeCreate = mt4TradeRepository.findAll().size();
        // Create the Mt4Trade
        restMt4TradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Trade)))
            .andExpect(status().isCreated());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeCreate + 1);
        Mt4Trade testMt4Trade = mt4TradeList.get(mt4TradeList.size() - 1);
        assertThat(testMt4Trade.getTicket()).isEqualByComparingTo(DEFAULT_TICKET);
        assertThat(testMt4Trade.getOpenTime()).isEqualTo(DEFAULT_OPEN_TIME);
        assertThat(testMt4Trade.getDirectionType()).isEqualTo(DEFAULT_DIRECTION_TYPE);
        assertThat(testMt4Trade.getPositionSize()).isEqualTo(DEFAULT_POSITION_SIZE);
        assertThat(testMt4Trade.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testMt4Trade.getOpenPrice()).isEqualTo(DEFAULT_OPEN_PRICE);
        assertThat(testMt4Trade.getStopLossPrice()).isEqualTo(DEFAULT_STOP_LOSS_PRICE);
        assertThat(testMt4Trade.getTakeProfitPrice()).isEqualTo(DEFAULT_TAKE_PROFIT_PRICE);
        assertThat(testMt4Trade.getCloseTime()).isEqualTo(DEFAULT_CLOSE_TIME);
        assertThat(testMt4Trade.getClosePrice()).isEqualTo(DEFAULT_CLOSE_PRICE);
        assertThat(testMt4Trade.getCommission()).isEqualTo(DEFAULT_COMMISSION);
        assertThat(testMt4Trade.getTaxes()).isEqualTo(DEFAULT_TAXES);
        assertThat(testMt4Trade.getSwap()).isEqualTo(DEFAULT_SWAP);
        assertThat(testMt4Trade.getProfit()).isEqualTo(DEFAULT_PROFIT);
    }

    @Test
    @Transactional
    void createMt4TradeWithExistingId() throws Exception {
        // Create the Mt4Trade with an existing ID
        mt4Trade.setId(1L);

        int databaseSizeBeforeCreate = mt4TradeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMt4TradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Trade)))
            .andExpect(status().isBadRequest());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTicketIsRequired() throws Exception {
        int databaseSizeBeforeTest = mt4TradeRepository.findAll().size();
        // set the field null
        mt4Trade.setTicket(null);

        // Create the Mt4Trade, which fails.

        restMt4TradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Trade)))
            .andExpect(status().isBadRequest());

        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMt4Trades() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mt4Trade.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(sameNumber(DEFAULT_TICKET))))
            .andExpect(jsonPath("$.[*].openTime").value(hasItem(DEFAULT_OPEN_TIME.toString())))
            .andExpect(jsonPath("$.[*].directionType").value(hasItem(DEFAULT_DIRECTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].positionSize").value(hasItem(DEFAULT_POSITION_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].openPrice").value(hasItem(DEFAULT_OPEN_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].stopLossPrice").value(hasItem(DEFAULT_STOP_LOSS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].takeProfitPrice").value(hasItem(DEFAULT_TAKE_PROFIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].closePrice").value(hasItem(DEFAULT_CLOSE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].commission").value(hasItem(DEFAULT_COMMISSION.doubleValue())))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.doubleValue())))
            .andExpect(jsonPath("$.[*].swap").value(hasItem(DEFAULT_SWAP.doubleValue())))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())));
    }

    @Test
    @Transactional
    void getMt4Trade() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get the mt4Trade
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL_ID, mt4Trade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mt4Trade.getId().intValue()))
            .andExpect(jsonPath("$.ticket").value(sameNumber(DEFAULT_TICKET)))
            .andExpect(jsonPath("$.openTime").value(DEFAULT_OPEN_TIME.toString()))
            .andExpect(jsonPath("$.directionType").value(DEFAULT_DIRECTION_TYPE.toString()))
            .andExpect(jsonPath("$.positionSize").value(DEFAULT_POSITION_SIZE.doubleValue()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL))
            .andExpect(jsonPath("$.openPrice").value(DEFAULT_OPEN_PRICE.doubleValue()))
            .andExpect(jsonPath("$.stopLossPrice").value(DEFAULT_STOP_LOSS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.takeProfitPrice").value(DEFAULT_TAKE_PROFIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.closeTime").value(DEFAULT_CLOSE_TIME.toString()))
            .andExpect(jsonPath("$.closePrice").value(DEFAULT_CLOSE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.commission").value(DEFAULT_COMMISSION.doubleValue()))
            .andExpect(jsonPath("$.taxes").value(DEFAULT_TAXES.doubleValue()))
            .andExpect(jsonPath("$.swap").value(DEFAULT_SWAP.doubleValue()))
            .andExpect(jsonPath("$.profit").value(DEFAULT_PROFIT.doubleValue()));
    }

    @Test
    @Transactional
    void getMt4TradesByIdFiltering() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        Long id = mt4Trade.getId();

        defaultMt4TradeShouldBeFound("id.equals=" + id);
        defaultMt4TradeShouldNotBeFound("id.notEquals=" + id);

        defaultMt4TradeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMt4TradeShouldNotBeFound("id.greaterThan=" + id);

        defaultMt4TradeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMt4TradeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket equals to DEFAULT_TICKET
        defaultMt4TradeShouldBeFound("ticket.equals=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket equals to UPDATED_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.equals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket not equals to DEFAULT_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.notEquals=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket not equals to UPDATED_TICKET
        defaultMt4TradeShouldBeFound("ticket.notEquals=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket in DEFAULT_TICKET or UPDATED_TICKET
        defaultMt4TradeShouldBeFound("ticket.in=" + DEFAULT_TICKET + "," + UPDATED_TICKET);

        // Get all the mt4TradeList where ticket equals to UPDATED_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.in=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket is not null
        defaultMt4TradeShouldBeFound("ticket.specified=true");

        // Get all the mt4TradeList where ticket is null
        defaultMt4TradeShouldNotBeFound("ticket.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket is greater than or equal to DEFAULT_TICKET
        defaultMt4TradeShouldBeFound("ticket.greaterThanOrEqual=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket is greater than or equal to UPDATED_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.greaterThanOrEqual=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket is less than or equal to DEFAULT_TICKET
        defaultMt4TradeShouldBeFound("ticket.lessThanOrEqual=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket is less than or equal to SMALLER_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.lessThanOrEqual=" + SMALLER_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket is less than DEFAULT_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.lessThan=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket is less than UPDATED_TICKET
        defaultMt4TradeShouldBeFound("ticket.lessThan=" + UPDATED_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTicketIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where ticket is greater than DEFAULT_TICKET
        defaultMt4TradeShouldNotBeFound("ticket.greaterThan=" + DEFAULT_TICKET);

        // Get all the mt4TradeList where ticket is greater than SMALLER_TICKET
        defaultMt4TradeShouldBeFound("ticket.greaterThan=" + SMALLER_TICKET);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openTime equals to DEFAULT_OPEN_TIME
        defaultMt4TradeShouldBeFound("openTime.equals=" + DEFAULT_OPEN_TIME);

        // Get all the mt4TradeList where openTime equals to UPDATED_OPEN_TIME
        defaultMt4TradeShouldNotBeFound("openTime.equals=" + UPDATED_OPEN_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openTime not equals to DEFAULT_OPEN_TIME
        defaultMt4TradeShouldNotBeFound("openTime.notEquals=" + DEFAULT_OPEN_TIME);

        // Get all the mt4TradeList where openTime not equals to UPDATED_OPEN_TIME
        defaultMt4TradeShouldBeFound("openTime.notEquals=" + UPDATED_OPEN_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenTimeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openTime in DEFAULT_OPEN_TIME or UPDATED_OPEN_TIME
        defaultMt4TradeShouldBeFound("openTime.in=" + DEFAULT_OPEN_TIME + "," + UPDATED_OPEN_TIME);

        // Get all the mt4TradeList where openTime equals to UPDATED_OPEN_TIME
        defaultMt4TradeShouldNotBeFound("openTime.in=" + UPDATED_OPEN_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openTime is not null
        defaultMt4TradeShouldBeFound("openTime.specified=true");

        // Get all the mt4TradeList where openTime is null
        defaultMt4TradeShouldNotBeFound("openTime.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByDirectionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where directionType equals to DEFAULT_DIRECTION_TYPE
        defaultMt4TradeShouldBeFound("directionType.equals=" + DEFAULT_DIRECTION_TYPE);

        // Get all the mt4TradeList where directionType equals to UPDATED_DIRECTION_TYPE
        defaultMt4TradeShouldNotBeFound("directionType.equals=" + UPDATED_DIRECTION_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByDirectionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where directionType not equals to DEFAULT_DIRECTION_TYPE
        defaultMt4TradeShouldNotBeFound("directionType.notEquals=" + DEFAULT_DIRECTION_TYPE);

        // Get all the mt4TradeList where directionType not equals to UPDATED_DIRECTION_TYPE
        defaultMt4TradeShouldBeFound("directionType.notEquals=" + UPDATED_DIRECTION_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByDirectionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where directionType in DEFAULT_DIRECTION_TYPE or UPDATED_DIRECTION_TYPE
        defaultMt4TradeShouldBeFound("directionType.in=" + DEFAULT_DIRECTION_TYPE + "," + UPDATED_DIRECTION_TYPE);

        // Get all the mt4TradeList where directionType equals to UPDATED_DIRECTION_TYPE
        defaultMt4TradeShouldNotBeFound("directionType.in=" + UPDATED_DIRECTION_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByDirectionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where directionType is not null
        defaultMt4TradeShouldBeFound("directionType.specified=true");

        // Get all the mt4TradeList where directionType is null
        defaultMt4TradeShouldNotBeFound("directionType.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize equals to DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.equals=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize equals to UPDATED_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.equals=" + UPDATED_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize not equals to DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.notEquals=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize not equals to UPDATED_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.notEquals=" + UPDATED_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize in DEFAULT_POSITION_SIZE or UPDATED_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.in=" + DEFAULT_POSITION_SIZE + "," + UPDATED_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize equals to UPDATED_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.in=" + UPDATED_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize is not null
        defaultMt4TradeShouldBeFound("positionSize.specified=true");

        // Get all the mt4TradeList where positionSize is null
        defaultMt4TradeShouldNotBeFound("positionSize.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize is greater than or equal to DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.greaterThanOrEqual=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize is greater than or equal to UPDATED_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.greaterThanOrEqual=" + UPDATED_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize is less than or equal to DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.lessThanOrEqual=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize is less than or equal to SMALLER_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.lessThanOrEqual=" + SMALLER_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize is less than DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.lessThan=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize is less than UPDATED_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.lessThan=" + UPDATED_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByPositionSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where positionSize is greater than DEFAULT_POSITION_SIZE
        defaultMt4TradeShouldNotBeFound("positionSize.greaterThan=" + DEFAULT_POSITION_SIZE);

        // Get all the mt4TradeList where positionSize is greater than SMALLER_POSITION_SIZE
        defaultMt4TradeShouldBeFound("positionSize.greaterThan=" + SMALLER_POSITION_SIZE);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol equals to DEFAULT_SYMBOL
        defaultMt4TradeShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the mt4TradeList where symbol equals to UPDATED_SYMBOL
        defaultMt4TradeShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol not equals to DEFAULT_SYMBOL
        defaultMt4TradeShouldNotBeFound("symbol.notEquals=" + DEFAULT_SYMBOL);

        // Get all the mt4TradeList where symbol not equals to UPDATED_SYMBOL
        defaultMt4TradeShouldBeFound("symbol.notEquals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultMt4TradeShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the mt4TradeList where symbol equals to UPDATED_SYMBOL
        defaultMt4TradeShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol is not null
        defaultMt4TradeShouldBeFound("symbol.specified=true");

        // Get all the mt4TradeList where symbol is null
        defaultMt4TradeShouldNotBeFound("symbol.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolContainsSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol contains DEFAULT_SYMBOL
        defaultMt4TradeShouldBeFound("symbol.contains=" + DEFAULT_SYMBOL);

        // Get all the mt4TradeList where symbol contains UPDATED_SYMBOL
        defaultMt4TradeShouldNotBeFound("symbol.contains=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySymbolNotContainsSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where symbol does not contain DEFAULT_SYMBOL
        defaultMt4TradeShouldNotBeFound("symbol.doesNotContain=" + DEFAULT_SYMBOL);

        // Get all the mt4TradeList where symbol does not contain UPDATED_SYMBOL
        defaultMt4TradeShouldBeFound("symbol.doesNotContain=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice equals to DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.equals=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice equals to UPDATED_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.equals=" + UPDATED_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice not equals to DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.notEquals=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice not equals to UPDATED_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.notEquals=" + UPDATED_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice in DEFAULT_OPEN_PRICE or UPDATED_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.in=" + DEFAULT_OPEN_PRICE + "," + UPDATED_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice equals to UPDATED_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.in=" + UPDATED_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice is not null
        defaultMt4TradeShouldBeFound("openPrice.specified=true");

        // Get all the mt4TradeList where openPrice is null
        defaultMt4TradeShouldNotBeFound("openPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice is greater than or equal to DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.greaterThanOrEqual=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice is greater than or equal to UPDATED_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.greaterThanOrEqual=" + UPDATED_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice is less than or equal to DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.lessThanOrEqual=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice is less than or equal to SMALLER_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.lessThanOrEqual=" + SMALLER_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice is less than DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.lessThan=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice is less than UPDATED_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.lessThan=" + UPDATED_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByOpenPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where openPrice is greater than DEFAULT_OPEN_PRICE
        defaultMt4TradeShouldNotBeFound("openPrice.greaterThan=" + DEFAULT_OPEN_PRICE);

        // Get all the mt4TradeList where openPrice is greater than SMALLER_OPEN_PRICE
        defaultMt4TradeShouldBeFound("openPrice.greaterThan=" + SMALLER_OPEN_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice equals to DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.equals=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice equals to UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.equals=" + UPDATED_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice not equals to DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.notEquals=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice not equals to UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.notEquals=" + UPDATED_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice in DEFAULT_STOP_LOSS_PRICE or UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.in=" + DEFAULT_STOP_LOSS_PRICE + "," + UPDATED_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice equals to UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.in=" + UPDATED_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice is not null
        defaultMt4TradeShouldBeFound("stopLossPrice.specified=true");

        // Get all the mt4TradeList where stopLossPrice is null
        defaultMt4TradeShouldNotBeFound("stopLossPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice is greater than or equal to DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.greaterThanOrEqual=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice is greater than or equal to UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.greaterThanOrEqual=" + UPDATED_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice is less than or equal to DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.lessThanOrEqual=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice is less than or equal to SMALLER_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.lessThanOrEqual=" + SMALLER_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice is less than DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.lessThan=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice is less than UPDATED_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.lessThan=" + UPDATED_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByStopLossPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where stopLossPrice is greater than DEFAULT_STOP_LOSS_PRICE
        defaultMt4TradeShouldNotBeFound("stopLossPrice.greaterThan=" + DEFAULT_STOP_LOSS_PRICE);

        // Get all the mt4TradeList where stopLossPrice is greater than SMALLER_STOP_LOSS_PRICE
        defaultMt4TradeShouldBeFound("stopLossPrice.greaterThan=" + SMALLER_STOP_LOSS_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice equals to DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.equals=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice equals to UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.equals=" + UPDATED_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice not equals to DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.notEquals=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice not equals to UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.notEquals=" + UPDATED_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice in DEFAULT_TAKE_PROFIT_PRICE or UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.in=" + DEFAULT_TAKE_PROFIT_PRICE + "," + UPDATED_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice equals to UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.in=" + UPDATED_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice is not null
        defaultMt4TradeShouldBeFound("takeProfitPrice.specified=true");

        // Get all the mt4TradeList where takeProfitPrice is null
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice is greater than or equal to DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.greaterThanOrEqual=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice is greater than or equal to UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.greaterThanOrEqual=" + UPDATED_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice is less than or equal to DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.lessThanOrEqual=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice is less than or equal to SMALLER_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.lessThanOrEqual=" + SMALLER_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice is less than DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.lessThan=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice is less than UPDATED_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.lessThan=" + UPDATED_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTakeProfitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where takeProfitPrice is greater than DEFAULT_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldNotBeFound("takeProfitPrice.greaterThan=" + DEFAULT_TAKE_PROFIT_PRICE);

        // Get all the mt4TradeList where takeProfitPrice is greater than SMALLER_TAKE_PROFIT_PRICE
        defaultMt4TradeShouldBeFound("takeProfitPrice.greaterThan=" + SMALLER_TAKE_PROFIT_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCloseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closeTime equals to DEFAULT_CLOSE_TIME
        defaultMt4TradeShouldBeFound("closeTime.equals=" + DEFAULT_CLOSE_TIME);

        // Get all the mt4TradeList where closeTime equals to UPDATED_CLOSE_TIME
        defaultMt4TradeShouldNotBeFound("closeTime.equals=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCloseTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closeTime not equals to DEFAULT_CLOSE_TIME
        defaultMt4TradeShouldNotBeFound("closeTime.notEquals=" + DEFAULT_CLOSE_TIME);

        // Get all the mt4TradeList where closeTime not equals to UPDATED_CLOSE_TIME
        defaultMt4TradeShouldBeFound("closeTime.notEquals=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCloseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closeTime in DEFAULT_CLOSE_TIME or UPDATED_CLOSE_TIME
        defaultMt4TradeShouldBeFound("closeTime.in=" + DEFAULT_CLOSE_TIME + "," + UPDATED_CLOSE_TIME);

        // Get all the mt4TradeList where closeTime equals to UPDATED_CLOSE_TIME
        defaultMt4TradeShouldNotBeFound("closeTime.in=" + UPDATED_CLOSE_TIME);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCloseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closeTime is not null
        defaultMt4TradeShouldBeFound("closeTime.specified=true");

        // Get all the mt4TradeList where closeTime is null
        defaultMt4TradeShouldNotBeFound("closeTime.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice equals to DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.equals=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice equals to UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.equals=" + UPDATED_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice not equals to DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.notEquals=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice not equals to UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.notEquals=" + UPDATED_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice in DEFAULT_CLOSE_PRICE or UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.in=" + DEFAULT_CLOSE_PRICE + "," + UPDATED_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice equals to UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.in=" + UPDATED_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice is not null
        defaultMt4TradeShouldBeFound("closePrice.specified=true");

        // Get all the mt4TradeList where closePrice is null
        defaultMt4TradeShouldNotBeFound("closePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice is greater than or equal to DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.greaterThanOrEqual=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice is greater than or equal to UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.greaterThanOrEqual=" + UPDATED_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice is less than or equal to DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.lessThanOrEqual=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice is less than or equal to SMALLER_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.lessThanOrEqual=" + SMALLER_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice is less than DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.lessThan=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice is less than UPDATED_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.lessThan=" + UPDATED_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByClosePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where closePrice is greater than DEFAULT_CLOSE_PRICE
        defaultMt4TradeShouldNotBeFound("closePrice.greaterThan=" + DEFAULT_CLOSE_PRICE);

        // Get all the mt4TradeList where closePrice is greater than SMALLER_CLOSE_PRICE
        defaultMt4TradeShouldBeFound("closePrice.greaterThan=" + SMALLER_CLOSE_PRICE);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission equals to DEFAULT_COMMISSION
        defaultMt4TradeShouldBeFound("commission.equals=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission equals to UPDATED_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.equals=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission not equals to DEFAULT_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.notEquals=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission not equals to UPDATED_COMMISSION
        defaultMt4TradeShouldBeFound("commission.notEquals=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission in DEFAULT_COMMISSION or UPDATED_COMMISSION
        defaultMt4TradeShouldBeFound("commission.in=" + DEFAULT_COMMISSION + "," + UPDATED_COMMISSION);

        // Get all the mt4TradeList where commission equals to UPDATED_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.in=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission is not null
        defaultMt4TradeShouldBeFound("commission.specified=true");

        // Get all the mt4TradeList where commission is null
        defaultMt4TradeShouldNotBeFound("commission.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission is greater than or equal to DEFAULT_COMMISSION
        defaultMt4TradeShouldBeFound("commission.greaterThanOrEqual=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission is greater than or equal to UPDATED_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.greaterThanOrEqual=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission is less than or equal to DEFAULT_COMMISSION
        defaultMt4TradeShouldBeFound("commission.lessThanOrEqual=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission is less than or equal to SMALLER_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.lessThanOrEqual=" + SMALLER_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission is less than DEFAULT_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.lessThan=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission is less than UPDATED_COMMISSION
        defaultMt4TradeShouldBeFound("commission.lessThan=" + UPDATED_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByCommissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where commission is greater than DEFAULT_COMMISSION
        defaultMt4TradeShouldNotBeFound("commission.greaterThan=" + DEFAULT_COMMISSION);

        // Get all the mt4TradeList where commission is greater than SMALLER_COMMISSION
        defaultMt4TradeShouldBeFound("commission.greaterThan=" + SMALLER_COMMISSION);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes equals to DEFAULT_TAXES
        defaultMt4TradeShouldBeFound("taxes.equals=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes equals to UPDATED_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.equals=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes not equals to DEFAULT_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.notEquals=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes not equals to UPDATED_TAXES
        defaultMt4TradeShouldBeFound("taxes.notEquals=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes in DEFAULT_TAXES or UPDATED_TAXES
        defaultMt4TradeShouldBeFound("taxes.in=" + DEFAULT_TAXES + "," + UPDATED_TAXES);

        // Get all the mt4TradeList where taxes equals to UPDATED_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.in=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes is not null
        defaultMt4TradeShouldBeFound("taxes.specified=true");

        // Get all the mt4TradeList where taxes is null
        defaultMt4TradeShouldNotBeFound("taxes.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes is greater than or equal to DEFAULT_TAXES
        defaultMt4TradeShouldBeFound("taxes.greaterThanOrEqual=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes is greater than or equal to UPDATED_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.greaterThanOrEqual=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes is less than or equal to DEFAULT_TAXES
        defaultMt4TradeShouldBeFound("taxes.lessThanOrEqual=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes is less than or equal to SMALLER_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.lessThanOrEqual=" + SMALLER_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes is less than DEFAULT_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.lessThan=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes is less than UPDATED_TAXES
        defaultMt4TradeShouldBeFound("taxes.lessThan=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where taxes is greater than DEFAULT_TAXES
        defaultMt4TradeShouldNotBeFound("taxes.greaterThan=" + DEFAULT_TAXES);

        // Get all the mt4TradeList where taxes is greater than SMALLER_TAXES
        defaultMt4TradeShouldBeFound("taxes.greaterThan=" + SMALLER_TAXES);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap equals to DEFAULT_SWAP
        defaultMt4TradeShouldBeFound("swap.equals=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap equals to UPDATED_SWAP
        defaultMt4TradeShouldNotBeFound("swap.equals=" + UPDATED_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap not equals to DEFAULT_SWAP
        defaultMt4TradeShouldNotBeFound("swap.notEquals=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap not equals to UPDATED_SWAP
        defaultMt4TradeShouldBeFound("swap.notEquals=" + UPDATED_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap in DEFAULT_SWAP or UPDATED_SWAP
        defaultMt4TradeShouldBeFound("swap.in=" + DEFAULT_SWAP + "," + UPDATED_SWAP);

        // Get all the mt4TradeList where swap equals to UPDATED_SWAP
        defaultMt4TradeShouldNotBeFound("swap.in=" + UPDATED_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap is not null
        defaultMt4TradeShouldBeFound("swap.specified=true");

        // Get all the mt4TradeList where swap is null
        defaultMt4TradeShouldNotBeFound("swap.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap is greater than or equal to DEFAULT_SWAP
        defaultMt4TradeShouldBeFound("swap.greaterThanOrEqual=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap is greater than or equal to UPDATED_SWAP
        defaultMt4TradeShouldNotBeFound("swap.greaterThanOrEqual=" + UPDATED_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap is less than or equal to DEFAULT_SWAP
        defaultMt4TradeShouldBeFound("swap.lessThanOrEqual=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap is less than or equal to SMALLER_SWAP
        defaultMt4TradeShouldNotBeFound("swap.lessThanOrEqual=" + SMALLER_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap is less than DEFAULT_SWAP
        defaultMt4TradeShouldNotBeFound("swap.lessThan=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap is less than UPDATED_SWAP
        defaultMt4TradeShouldBeFound("swap.lessThan=" + UPDATED_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesBySwapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where swap is greater than DEFAULT_SWAP
        defaultMt4TradeShouldNotBeFound("swap.greaterThan=" + DEFAULT_SWAP);

        // Get all the mt4TradeList where swap is greater than SMALLER_SWAP
        defaultMt4TradeShouldBeFound("swap.greaterThan=" + SMALLER_SWAP);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit equals to DEFAULT_PROFIT
        defaultMt4TradeShouldBeFound("profit.equals=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit equals to UPDATED_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.equals=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit not equals to DEFAULT_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.notEquals=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit not equals to UPDATED_PROFIT
        defaultMt4TradeShouldBeFound("profit.notEquals=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsInShouldWork() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit in DEFAULT_PROFIT or UPDATED_PROFIT
        defaultMt4TradeShouldBeFound("profit.in=" + DEFAULT_PROFIT + "," + UPDATED_PROFIT);

        // Get all the mt4TradeList where profit equals to UPDATED_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.in=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit is not null
        defaultMt4TradeShouldBeFound("profit.specified=true");

        // Get all the mt4TradeList where profit is null
        defaultMt4TradeShouldNotBeFound("profit.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit is greater than or equal to DEFAULT_PROFIT
        defaultMt4TradeShouldBeFound("profit.greaterThanOrEqual=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit is greater than or equal to UPDATED_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.greaterThanOrEqual=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit is less than or equal to DEFAULT_PROFIT
        defaultMt4TradeShouldBeFound("profit.lessThanOrEqual=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit is less than or equal to SMALLER_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.lessThanOrEqual=" + SMALLER_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit is less than DEFAULT_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.lessThan=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit is less than UPDATED_PROFIT
        defaultMt4TradeShouldBeFound("profit.lessThan=" + UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        // Get all the mt4TradeList where profit is greater than DEFAULT_PROFIT
        defaultMt4TradeShouldNotBeFound("profit.greaterThan=" + DEFAULT_PROFIT);

        // Get all the mt4TradeList where profit is greater than SMALLER_PROFIT
        defaultMt4TradeShouldBeFound("profit.greaterThan=" + SMALLER_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4TradesByTradeJournalPostIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);
        TradeJournalPost tradeJournalPost = TradeJournalPostResourceIT.createEntity(em);
        em.persist(tradeJournalPost);
        em.flush();
        mt4Trade.setTradeJournalPost(tradeJournalPost);
        mt4TradeRepository.saveAndFlush(mt4Trade);
        Long tradeJournalPostId = tradeJournalPost.getId();

        // Get all the mt4TradeList where tradeJournalPost equals to tradeJournalPostId
        defaultMt4TradeShouldBeFound("tradeJournalPostId.equals=" + tradeJournalPostId);

        // Get all the mt4TradeList where tradeJournalPost equals to (tradeJournalPostId + 1)
        defaultMt4TradeShouldNotBeFound("tradeJournalPostId.equals=" + (tradeJournalPostId + 1));
    }

    @Test
    @Transactional
    void getAllMt4TradesByMt4AccountIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);
        Mt4Account mt4Account = Mt4AccountResourceIT.createEntity(em);
        em.persist(mt4Account);
        em.flush();
        mt4Trade.setMt4Account(mt4Account);
        mt4TradeRepository.saveAndFlush(mt4Trade);
        Long mt4AccountId = mt4Account.getId();

        // Get all the mt4TradeList where mt4Account equals to mt4AccountId
        defaultMt4TradeShouldBeFound("mt4AccountId.equals=" + mt4AccountId);

        // Get all the mt4TradeList where mt4Account equals to (mt4AccountId + 1)
        defaultMt4TradeShouldNotBeFound("mt4AccountId.equals=" + (mt4AccountId + 1));
    }

    @Test
    @Transactional
    void getAllMt4TradesByInstrumentIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);
        Instrument instrument = InstrumentResourceIT.createEntity(em);
        em.persist(instrument);
        em.flush();
        mt4Trade.setInstrument(instrument);
        mt4TradeRepository.saveAndFlush(mt4Trade);
        Long instrumentId = instrument.getId();

        // Get all the mt4TradeList where instrument equals to instrumentId
        defaultMt4TradeShouldBeFound("instrumentId.equals=" + instrumentId);

        // Get all the mt4TradeList where instrument equals to (instrumentId + 1)
        defaultMt4TradeShouldNotBeFound("instrumentId.equals=" + (instrumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMt4TradeShouldBeFound(String filter) throws Exception {
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mt4Trade.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticket").value(hasItem(sameNumber(DEFAULT_TICKET))))
            .andExpect(jsonPath("$.[*].openTime").value(hasItem(DEFAULT_OPEN_TIME.toString())))
            .andExpect(jsonPath("$.[*].directionType").value(hasItem(DEFAULT_DIRECTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].positionSize").value(hasItem(DEFAULT_POSITION_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL)))
            .andExpect(jsonPath("$.[*].openPrice").value(hasItem(DEFAULT_OPEN_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].stopLossPrice").value(hasItem(DEFAULT_STOP_LOSS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].takeProfitPrice").value(hasItem(DEFAULT_TAKE_PROFIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].closeTime").value(hasItem(DEFAULT_CLOSE_TIME.toString())))
            .andExpect(jsonPath("$.[*].closePrice").value(hasItem(DEFAULT_CLOSE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].commission").value(hasItem(DEFAULT_COMMISSION.doubleValue())))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.doubleValue())))
            .andExpect(jsonPath("$.[*].swap").value(hasItem(DEFAULT_SWAP.doubleValue())))
            .andExpect(jsonPath("$.[*].profit").value(hasItem(DEFAULT_PROFIT.doubleValue())));

        // Check, that the count call also returns 1
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMt4TradeShouldNotBeFound(String filter) throws Exception {
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMt4TradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMt4Trade() throws Exception {
        // Get the mt4Trade
        restMt4TradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMt4Trade() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();

        // Update the mt4Trade
        Mt4Trade updatedMt4Trade = mt4TradeRepository.findById(mt4Trade.getId()).get();
        // Disconnect from session so that the updates on updatedMt4Trade are not directly saved in db
        em.detach(updatedMt4Trade);
        updatedMt4Trade
            .ticket(UPDATED_TICKET)
            .openTime(UPDATED_OPEN_TIME)
            .directionType(UPDATED_DIRECTION_TYPE)
            .positionSize(UPDATED_POSITION_SIZE)
            .symbol(UPDATED_SYMBOL)
            .openPrice(UPDATED_OPEN_PRICE)
            .stopLossPrice(UPDATED_STOP_LOSS_PRICE)
            .takeProfitPrice(UPDATED_TAKE_PROFIT_PRICE)
            .closeTime(UPDATED_CLOSE_TIME)
            .closePrice(UPDATED_CLOSE_PRICE)
            .commission(UPDATED_COMMISSION)
            .taxes(UPDATED_TAXES)
            .swap(UPDATED_SWAP)
            .profit(UPDATED_PROFIT);

        restMt4TradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMt4Trade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMt4Trade))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
        Mt4Trade testMt4Trade = mt4TradeList.get(mt4TradeList.size() - 1);
        assertThat(testMt4Trade.getTicket()).isEqualTo(UPDATED_TICKET);
        assertThat(testMt4Trade.getOpenTime()).isEqualTo(UPDATED_OPEN_TIME);
        assertThat(testMt4Trade.getDirectionType()).isEqualTo(UPDATED_DIRECTION_TYPE);
        assertThat(testMt4Trade.getPositionSize()).isEqualTo(UPDATED_POSITION_SIZE);
        assertThat(testMt4Trade.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testMt4Trade.getOpenPrice()).isEqualTo(UPDATED_OPEN_PRICE);
        assertThat(testMt4Trade.getStopLossPrice()).isEqualTo(UPDATED_STOP_LOSS_PRICE);
        assertThat(testMt4Trade.getTakeProfitPrice()).isEqualTo(UPDATED_TAKE_PROFIT_PRICE);
        assertThat(testMt4Trade.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testMt4Trade.getClosePrice()).isEqualTo(UPDATED_CLOSE_PRICE);
        assertThat(testMt4Trade.getCommission()).isEqualTo(UPDATED_COMMISSION);
        assertThat(testMt4Trade.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testMt4Trade.getSwap()).isEqualTo(UPDATED_SWAP);
        assertThat(testMt4Trade.getProfit()).isEqualTo(UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void putNonExistingMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mt4Trade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mt4Trade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mt4Trade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Trade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMt4TradeWithPatch() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();

        // Update the mt4Trade using partial update
        Mt4Trade partialUpdatedMt4Trade = new Mt4Trade();
        partialUpdatedMt4Trade.setId(mt4Trade.getId());

        partialUpdatedMt4Trade
            .ticket(UPDATED_TICKET)
            .openTime(UPDATED_OPEN_TIME)
            .directionType(UPDATED_DIRECTION_TYPE)
            .closeTime(UPDATED_CLOSE_TIME)
            .closePrice(UPDATED_CLOSE_PRICE)
            .taxes(UPDATED_TAXES)
            .swap(UPDATED_SWAP)
            .profit(UPDATED_PROFIT);

        restMt4TradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMt4Trade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMt4Trade))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
        Mt4Trade testMt4Trade = mt4TradeList.get(mt4TradeList.size() - 1);
        assertThat(testMt4Trade.getTicket()).isEqualByComparingTo(UPDATED_TICKET);
        assertThat(testMt4Trade.getOpenTime()).isEqualTo(UPDATED_OPEN_TIME);
        assertThat(testMt4Trade.getDirectionType()).isEqualTo(UPDATED_DIRECTION_TYPE);
        assertThat(testMt4Trade.getPositionSize()).isEqualTo(DEFAULT_POSITION_SIZE);
        assertThat(testMt4Trade.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testMt4Trade.getOpenPrice()).isEqualTo(DEFAULT_OPEN_PRICE);
        assertThat(testMt4Trade.getStopLossPrice()).isEqualTo(DEFAULT_STOP_LOSS_PRICE);
        assertThat(testMt4Trade.getTakeProfitPrice()).isEqualTo(DEFAULT_TAKE_PROFIT_PRICE);
        assertThat(testMt4Trade.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testMt4Trade.getClosePrice()).isEqualTo(UPDATED_CLOSE_PRICE);
        assertThat(testMt4Trade.getCommission()).isEqualTo(DEFAULT_COMMISSION);
        assertThat(testMt4Trade.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testMt4Trade.getSwap()).isEqualTo(UPDATED_SWAP);
        assertThat(testMt4Trade.getProfit()).isEqualTo(UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void fullUpdateMt4TradeWithPatch() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();

        // Update the mt4Trade using partial update
        Mt4Trade partialUpdatedMt4Trade = new Mt4Trade();
        partialUpdatedMt4Trade.setId(mt4Trade.getId());

        partialUpdatedMt4Trade
            .ticket(UPDATED_TICKET)
            .openTime(UPDATED_OPEN_TIME)
            .directionType(UPDATED_DIRECTION_TYPE)
            .positionSize(UPDATED_POSITION_SIZE)
            .symbol(UPDATED_SYMBOL)
            .openPrice(UPDATED_OPEN_PRICE)
            .stopLossPrice(UPDATED_STOP_LOSS_PRICE)
            .takeProfitPrice(UPDATED_TAKE_PROFIT_PRICE)
            .closeTime(UPDATED_CLOSE_TIME)
            .closePrice(UPDATED_CLOSE_PRICE)
            .commission(UPDATED_COMMISSION)
            .taxes(UPDATED_TAXES)
            .swap(UPDATED_SWAP)
            .profit(UPDATED_PROFIT);

        restMt4TradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMt4Trade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMt4Trade))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
        Mt4Trade testMt4Trade = mt4TradeList.get(mt4TradeList.size() - 1);
        assertThat(testMt4Trade.getTicket()).isEqualByComparingTo(UPDATED_TICKET);
        assertThat(testMt4Trade.getOpenTime()).isEqualTo(UPDATED_OPEN_TIME);
        assertThat(testMt4Trade.getDirectionType()).isEqualTo(UPDATED_DIRECTION_TYPE);
        assertThat(testMt4Trade.getPositionSize()).isEqualTo(UPDATED_POSITION_SIZE);
        assertThat(testMt4Trade.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testMt4Trade.getOpenPrice()).isEqualTo(UPDATED_OPEN_PRICE);
        assertThat(testMt4Trade.getStopLossPrice()).isEqualTo(UPDATED_STOP_LOSS_PRICE);
        assertThat(testMt4Trade.getTakeProfitPrice()).isEqualTo(UPDATED_TAKE_PROFIT_PRICE);
        assertThat(testMt4Trade.getCloseTime()).isEqualTo(UPDATED_CLOSE_TIME);
        assertThat(testMt4Trade.getClosePrice()).isEqualTo(UPDATED_CLOSE_PRICE);
        assertThat(testMt4Trade.getCommission()).isEqualTo(UPDATED_COMMISSION);
        assertThat(testMt4Trade.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testMt4Trade.getSwap()).isEqualTo(UPDATED_SWAP);
        assertThat(testMt4Trade.getProfit()).isEqualTo(UPDATED_PROFIT);
    }

    @Test
    @Transactional
    void patchNonExistingMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mt4Trade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mt4Trade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mt4Trade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMt4Trade() throws Exception {
        int databaseSizeBeforeUpdate = mt4TradeRepository.findAll().size();
        mt4Trade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4TradeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mt4Trade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mt4Trade in the database
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMt4Trade() throws Exception {
        // Initialize the database
        mt4TradeRepository.saveAndFlush(mt4Trade);

        int databaseSizeBeforeDelete = mt4TradeRepository.findAll().size();

        // Delete the mt4Trade
        restMt4TradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, mt4Trade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mt4Trade> mt4TradeList = mt4TradeRepository.findAll();
        assertThat(mt4TradeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
