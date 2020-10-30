package io.github.leases.web.rest;

import io.github.leases.LeasesApp;
import io.github.leases.domain.LeasesMessageToken;
import io.github.leases.repository.LeasesMessageTokenRepository;
import io.github.leases.repository.search.LeasesMessageTokenSearchRepository;
import io.github.leases.service.LeasesMessageTokenService;
import io.github.leases.service.dto.LeasesMessageTokenDTO;
import io.github.leases.service.mapper.LeasesMessageTokenMapper;
import io.github.leases.service.dto.LeasesMessageTokenCriteria;
import io.github.leases.service.LeasesMessageTokenQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LeasesMessageTokenResource} REST controller.
 */
@SpringBootTest(classes = LeasesApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeasesMessageTokenResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_SENT = 1L;
    private static final Long UPDATED_TIME_SENT = 2L;
    private static final Long SMALLER_TIME_SENT = 1L - 1L;

    private static final String DEFAULT_TOKEN_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECEIVED = false;
    private static final Boolean UPDATED_RECEIVED = true;

    private static final Boolean DEFAULT_ACTIONED = false;
    private static final Boolean UPDATED_ACTIONED = true;

    private static final Boolean DEFAULT_CONTENT_FULLY_ENQUEUED = false;
    private static final Boolean UPDATED_CONTENT_FULLY_ENQUEUED = true;

    @Autowired
    private LeasesMessageTokenRepository leasesMessageTokenRepository;

    @Autowired
    private LeasesMessageTokenMapper leasesMessageTokenMapper;

    @Autowired
    private LeasesMessageTokenService leasesMessageTokenService;

    /**
     * This repository is mocked in the io.github.leases.repository.search test package.
     *
     * @see io.github.leases.repository.search.LeasesMessageTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeasesMessageTokenSearchRepository mockLeasesMessageTokenSearchRepository;

    @Autowired
    private LeasesMessageTokenQueryService leasesMessageTokenQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeasesMessageTokenMockMvc;

    private LeasesMessageToken leasesMessageToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesMessageToken createEntity(EntityManager em) {
        LeasesMessageToken leasesMessageToken = new LeasesMessageToken()
            .description(DEFAULT_DESCRIPTION)
            .timeSent(DEFAULT_TIME_SENT)
            .tokenValue(DEFAULT_TOKEN_VALUE)
            .received(DEFAULT_RECEIVED)
            .actioned(DEFAULT_ACTIONED)
            .contentFullyEnqueued(DEFAULT_CONTENT_FULLY_ENQUEUED);
        return leasesMessageToken;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesMessageToken createUpdatedEntity(EntityManager em) {
        LeasesMessageToken leasesMessageToken = new LeasesMessageToken()
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        return leasesMessageToken;
    }

    @BeforeEach
    public void initTest() {
        leasesMessageToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeasesMessageToken() throws Exception {
        int databaseSizeBeforeCreate = leasesMessageTokenRepository.findAll().size();
        // Create the LeasesMessageToken
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(leasesMessageToken);
        restLeasesMessageTokenMockMvc.perform(post("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isCreated());

        // Validate the LeasesMessageToken in the database
        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeCreate + 1);
        LeasesMessageToken testLeasesMessageToken = leasesMessageTokenList.get(leasesMessageTokenList.size() - 1);
        assertThat(testLeasesMessageToken.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeasesMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testLeasesMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testLeasesMessageToken.isReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testLeasesMessageToken.isActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testLeasesMessageToken.isContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Validate the LeasesMessageToken in Elasticsearch
        verify(mockLeasesMessageTokenSearchRepository, times(1)).save(testLeasesMessageToken);
    }

    @Test
    @Transactional
    public void createLeasesMessageTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leasesMessageTokenRepository.findAll().size();

        // Create the LeasesMessageToken with an existing ID
        leasesMessageToken.setId(1L);
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(leasesMessageToken);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeasesMessageTokenMockMvc.perform(post("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesMessageToken in the database
        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeasesMessageToken in Elasticsearch
        verify(mockLeasesMessageTokenSearchRepository, times(0)).save(leasesMessageToken);
    }


    @Test
    @Transactional
    public void checkTimeSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesMessageTokenRepository.findAll().size();
        // set the field null
        leasesMessageToken.setTimeSent(null);

        // Create the LeasesMessageToken, which fails.
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(leasesMessageToken);


        restLeasesMessageTokenMockMvc.perform(post("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokenValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesMessageTokenRepository.findAll().size();
        // set the field null
        leasesMessageToken.setTokenValue(null);

        // Create the LeasesMessageToken, which fails.
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(leasesMessageToken);


        restLeasesMessageTokenMockMvc.perform(post("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokens() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getLeasesMessageToken() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get the leasesMessageToken
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens/{id}", leasesMessageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leasesMessageToken.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.timeSent").value(DEFAULT_TIME_SENT.intValue()))
            .andExpect(jsonPath("$.tokenValue").value(DEFAULT_TOKEN_VALUE))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.booleanValue()))
            .andExpect(jsonPath("$.actioned").value(DEFAULT_ACTIONED.booleanValue()))
            .andExpect(jsonPath("$.contentFullyEnqueued").value(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue()));
    }


    @Test
    @Transactional
    public void getLeasesMessageTokensByIdFiltering() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        Long id = leasesMessageToken.getId();

        defaultLeasesMessageTokenShouldBeFound("id.equals=" + id);
        defaultLeasesMessageTokenShouldNotBeFound("id.notEquals=" + id);

        defaultLeasesMessageTokenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeasesMessageTokenShouldNotBeFound("id.greaterThan=" + id);

        defaultLeasesMessageTokenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeasesMessageTokenShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description equals to DEFAULT_DESCRIPTION
        defaultLeasesMessageTokenShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesMessageTokenList where description equals to UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description not equals to DEFAULT_DESCRIPTION
        defaultLeasesMessageTokenShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesMessageTokenList where description not equals to UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leasesMessageTokenList where description equals to UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description is not null
        defaultLeasesMessageTokenShouldBeFound("description.specified=true");

        // Get all the leasesMessageTokenList where description is null
        defaultLeasesMessageTokenShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description contains DEFAULT_DESCRIPTION
        defaultLeasesMessageTokenShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leasesMessageTokenList where description contains UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where description does not contain DEFAULT_DESCRIPTION
        defaultLeasesMessageTokenShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leasesMessageTokenList where description does not contain UPDATED_DESCRIPTION
        defaultLeasesMessageTokenShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent equals to DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.equals=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.equals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent not equals to DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.notEquals=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent not equals to UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.notEquals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent in DEFAULT_TIME_SENT or UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.in=" + DEFAULT_TIME_SENT + "," + UPDATED_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.in=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent is not null
        defaultLeasesMessageTokenShouldBeFound("timeSent.specified=true");

        // Get all the leasesMessageTokenList where timeSent is null
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent is greater than or equal to DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.greaterThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent is greater than or equal to UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.greaterThanOrEqual=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent is less than or equal to DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.lessThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent is less than or equal to SMALLER_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.lessThanOrEqual=" + SMALLER_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsLessThanSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent is less than DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.lessThan=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent is less than UPDATED_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.lessThan=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTimeSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where timeSent is greater than DEFAULT_TIME_SENT
        defaultLeasesMessageTokenShouldNotBeFound("timeSent.greaterThan=" + DEFAULT_TIME_SENT);

        // Get all the leasesMessageTokenList where timeSent is greater than SMALLER_TIME_SENT
        defaultLeasesMessageTokenShouldBeFound("timeSent.greaterThan=" + SMALLER_TIME_SENT);
    }


    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue equals to DEFAULT_TOKEN_VALUE
        defaultLeasesMessageTokenShouldBeFound("tokenValue.equals=" + DEFAULT_TOKEN_VALUE);

        // Get all the leasesMessageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.equals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue not equals to DEFAULT_TOKEN_VALUE
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.notEquals=" + DEFAULT_TOKEN_VALUE);

        // Get all the leasesMessageTokenList where tokenValue not equals to UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldBeFound("tokenValue.notEquals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue in DEFAULT_TOKEN_VALUE or UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldBeFound("tokenValue.in=" + DEFAULT_TOKEN_VALUE + "," + UPDATED_TOKEN_VALUE);

        // Get all the leasesMessageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.in=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue is not null
        defaultLeasesMessageTokenShouldBeFound("tokenValue.specified=true");

        // Get all the leasesMessageTokenList where tokenValue is null
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueContainsSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue contains DEFAULT_TOKEN_VALUE
        defaultLeasesMessageTokenShouldBeFound("tokenValue.contains=" + DEFAULT_TOKEN_VALUE);

        // Get all the leasesMessageTokenList where tokenValue contains UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.contains=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByTokenValueNotContainsSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where tokenValue does not contain DEFAULT_TOKEN_VALUE
        defaultLeasesMessageTokenShouldNotBeFound("tokenValue.doesNotContain=" + DEFAULT_TOKEN_VALUE);

        // Get all the leasesMessageTokenList where tokenValue does not contain UPDATED_TOKEN_VALUE
        defaultLeasesMessageTokenShouldBeFound("tokenValue.doesNotContain=" + UPDATED_TOKEN_VALUE);
    }


    @Test
    @Transactional
    public void getAllLeasesMessageTokensByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where received equals to DEFAULT_RECEIVED
        defaultLeasesMessageTokenShouldBeFound("received.equals=" + DEFAULT_RECEIVED);

        // Get all the leasesMessageTokenList where received equals to UPDATED_RECEIVED
        defaultLeasesMessageTokenShouldNotBeFound("received.equals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where received not equals to DEFAULT_RECEIVED
        defaultLeasesMessageTokenShouldNotBeFound("received.notEquals=" + DEFAULT_RECEIVED);

        // Get all the leasesMessageTokenList where received not equals to UPDATED_RECEIVED
        defaultLeasesMessageTokenShouldBeFound("received.notEquals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where received in DEFAULT_RECEIVED or UPDATED_RECEIVED
        defaultLeasesMessageTokenShouldBeFound("received.in=" + DEFAULT_RECEIVED + "," + UPDATED_RECEIVED);

        // Get all the leasesMessageTokenList where received equals to UPDATED_RECEIVED
        defaultLeasesMessageTokenShouldNotBeFound("received.in=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where received is not null
        defaultLeasesMessageTokenShouldBeFound("received.specified=true");

        // Get all the leasesMessageTokenList where received is null
        defaultLeasesMessageTokenShouldNotBeFound("received.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByActionedIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where actioned equals to DEFAULT_ACTIONED
        defaultLeasesMessageTokenShouldBeFound("actioned.equals=" + DEFAULT_ACTIONED);

        // Get all the leasesMessageTokenList where actioned equals to UPDATED_ACTIONED
        defaultLeasesMessageTokenShouldNotBeFound("actioned.equals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByActionedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where actioned not equals to DEFAULT_ACTIONED
        defaultLeasesMessageTokenShouldNotBeFound("actioned.notEquals=" + DEFAULT_ACTIONED);

        // Get all the leasesMessageTokenList where actioned not equals to UPDATED_ACTIONED
        defaultLeasesMessageTokenShouldBeFound("actioned.notEquals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByActionedIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where actioned in DEFAULT_ACTIONED or UPDATED_ACTIONED
        defaultLeasesMessageTokenShouldBeFound("actioned.in=" + DEFAULT_ACTIONED + "," + UPDATED_ACTIONED);

        // Get all the leasesMessageTokenList where actioned equals to UPDATED_ACTIONED
        defaultLeasesMessageTokenShouldNotBeFound("actioned.in=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByActionedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where actioned is not null
        defaultLeasesMessageTokenShouldBeFound("actioned.specified=true");

        // Get all the leasesMessageTokenList where actioned is null
        defaultLeasesMessageTokenShouldNotBeFound("actioned.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByContentFullyEnqueuedIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where contentFullyEnqueued equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldBeFound("contentFullyEnqueued.equals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the leasesMessageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldNotBeFound("contentFullyEnqueued.equals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByContentFullyEnqueuedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where contentFullyEnqueued not equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldNotBeFound("contentFullyEnqueued.notEquals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the leasesMessageTokenList where contentFullyEnqueued not equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldBeFound("contentFullyEnqueued.notEquals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByContentFullyEnqueuedIsInShouldWork() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where contentFullyEnqueued in DEFAULT_CONTENT_FULLY_ENQUEUED or UPDATED_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldBeFound("contentFullyEnqueued.in=" + DEFAULT_CONTENT_FULLY_ENQUEUED + "," + UPDATED_CONTENT_FULLY_ENQUEUED);

        // Get all the leasesMessageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultLeasesMessageTokenShouldNotBeFound("contentFullyEnqueued.in=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllLeasesMessageTokensByContentFullyEnqueuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        // Get all the leasesMessageTokenList where contentFullyEnqueued is not null
        defaultLeasesMessageTokenShouldBeFound("contentFullyEnqueued.specified=true");

        // Get all the leasesMessageTokenList where contentFullyEnqueued is null
        defaultLeasesMessageTokenShouldNotBeFound("contentFullyEnqueued.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeasesMessageTokenShouldBeFound(String filter) throws Exception {
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));

        // Check, that the count call also returns 1
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeasesMessageTokenShouldNotBeFound(String filter) throws Exception {
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeasesMessageToken() throws Exception {
        // Get the leasesMessageToken
        restLeasesMessageTokenMockMvc.perform(get("/api/leases-message-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeasesMessageToken() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        int databaseSizeBeforeUpdate = leasesMessageTokenRepository.findAll().size();

        // Update the leasesMessageToken
        LeasesMessageToken updatedLeasesMessageToken = leasesMessageTokenRepository.findById(leasesMessageToken.getId()).get();
        // Disconnect from session so that the updates on updatedLeasesMessageToken are not directly saved in db
        em.detach(updatedLeasesMessageToken);
        updatedLeasesMessageToken
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(updatedLeasesMessageToken);

        restLeasesMessageTokenMockMvc.perform(put("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isOk());

        // Validate the LeasesMessageToken in the database
        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeUpdate);
        LeasesMessageToken testLeasesMessageToken = leasesMessageTokenList.get(leasesMessageTokenList.size() - 1);
        assertThat(testLeasesMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeasesMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testLeasesMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testLeasesMessageToken.isReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testLeasesMessageToken.isActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testLeasesMessageToken.isContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);

        // Validate the LeasesMessageToken in Elasticsearch
        verify(mockLeasesMessageTokenSearchRepository, times(1)).save(testLeasesMessageToken);
    }

    @Test
    @Transactional
    public void updateNonExistingLeasesMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = leasesMessageTokenRepository.findAll().size();

        // Create the LeasesMessageToken
        LeasesMessageTokenDTO leasesMessageTokenDTO = leasesMessageTokenMapper.toDto(leasesMessageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasesMessageTokenMockMvc.perform(put("/api/leases-message-tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesMessageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesMessageToken in the database
        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasesMessageToken in Elasticsearch
        verify(mockLeasesMessageTokenSearchRepository, times(0)).save(leasesMessageToken);
    }

    @Test
    @Transactional
    public void deleteLeasesMessageToken() throws Exception {
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);

        int databaseSizeBeforeDelete = leasesMessageTokenRepository.findAll().size();

        // Delete the leasesMessageToken
        restLeasesMessageTokenMockMvc.perform(delete("/api/leases-message-tokens/{id}", leasesMessageToken.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeasesMessageToken> leasesMessageTokenList = leasesMessageTokenRepository.findAll();
        assertThat(leasesMessageTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeasesMessageToken in Elasticsearch
        verify(mockLeasesMessageTokenSearchRepository, times(1)).deleteById(leasesMessageToken.getId());
    }

    @Test
    @Transactional
    public void searchLeasesMessageToken() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leasesMessageTokenRepository.saveAndFlush(leasesMessageToken);
        when(mockLeasesMessageTokenSearchRepository.search(queryStringQuery("id:" + leasesMessageToken.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leasesMessageToken), PageRequest.of(0, 1), 1));

        // Search the leasesMessageToken
        restLeasesMessageTokenMockMvc.perform(get("/api/_search/leases-message-tokens?query=id:" + leasesMessageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesMessageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
}
