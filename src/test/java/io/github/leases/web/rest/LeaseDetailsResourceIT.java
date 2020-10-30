package io.github.leases.web.rest;

import io.github.leases.LeasesApp;
import io.github.leases.domain.LeaseDetails;
import io.github.leases.repository.LeaseDetailsRepository;
import io.github.leases.repository.search.LeaseDetailsSearchRepository;
import io.github.leases.service.LeaseDetailsService;
import io.github.leases.service.dto.LeaseDetailsDTO;
import io.github.leases.service.mapper.LeaseDetailsMapper;
import io.github.leases.service.dto.LeaseDetailsCriteria;
import io.github.leases.service.LeaseDetailsQueryService;

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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LeaseDetailsResource} REST controller.
 */
@SpringBootTest(classes = LeasesApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeaseDetailsResourceIT {

    private static final String DEFAULT_LEASE_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_INCREMENTAL_BORROWING_RATE = 1D;
    private static final Double UPDATED_INCREMENTAL_BORROWING_RATE = 2D;
    private static final Double SMALLER_INCREMENTAL_BORROWING_RATE = 1D - 1D;

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LEASE_PREPAYMENTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEASE_PREPAYMENTS = new BigDecimal(2);
    private static final BigDecimal SMALLER_LEASE_PREPAYMENTS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INITIAL_DIRECT_COSTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_INITIAL_DIRECT_COSTS = new BigDecimal(2);
    private static final BigDecimal SMALLER_INITIAL_DIRECT_COSTS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEMOLITION_COSTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEMOLITION_COSTS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEMOLITION_COSTS = new BigDecimal(1 - 1);

    private static final String DEFAULT_ASSET_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LIABILITY_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LIABILITY_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DEPRECIATION_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DEPRECIATION_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INTEREST_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_ACCOUNT_NUMBER = "BBBBBBBBBB";

    @Autowired
    private LeaseDetailsRepository leaseDetailsRepository;

    @Autowired
    private LeaseDetailsMapper leaseDetailsMapper;

    @Autowired
    private LeaseDetailsService leaseDetailsService;

    /**
     * This repository is mocked in the io.github.leases.repository.search test package.
     *
     * @see io.github.leases.repository.search.LeaseDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseDetailsSearchRepository mockLeaseDetailsSearchRepository;

    @Autowired
    private LeaseDetailsQueryService leaseDetailsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseDetailsMockMvc;

    private LeaseDetails leaseDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseDetails createEntity(EntityManager em) {
        LeaseDetails leaseDetails = new LeaseDetails()
            .leaseContractNumber(DEFAULT_LEASE_CONTRACT_NUMBER)
            .incrementalBorrowingRate(DEFAULT_INCREMENTAL_BORROWING_RATE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .leasePrepayments(DEFAULT_LEASE_PREPAYMENTS)
            .initialDirectCosts(DEFAULT_INITIAL_DIRECT_COSTS)
            .demolitionCosts(DEFAULT_DEMOLITION_COSTS)
            .assetAccountNumber(DEFAULT_ASSET_ACCOUNT_NUMBER)
            .liabilityAccountNumber(DEFAULT_LIABILITY_ACCOUNT_NUMBER)
            .depreciationAccountNumber(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)
            .interestAccountNumber(DEFAULT_INTEREST_ACCOUNT_NUMBER);
        return leaseDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseDetails createUpdatedEntity(EntityManager em) {
        LeaseDetails leaseDetails = new LeaseDetails()
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .incrementalBorrowingRate(UPDATED_INCREMENTAL_BORROWING_RATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .leasePrepayments(UPDATED_LEASE_PREPAYMENTS)
            .initialDirectCosts(UPDATED_INITIAL_DIRECT_COSTS)
            .demolitionCosts(UPDATED_DEMOLITION_COSTS)
            .assetAccountNumber(UPDATED_ASSET_ACCOUNT_NUMBER)
            .liabilityAccountNumber(UPDATED_LIABILITY_ACCOUNT_NUMBER)
            .depreciationAccountNumber(UPDATED_DEPRECIATION_ACCOUNT_NUMBER)
            .interestAccountNumber(UPDATED_INTEREST_ACCOUNT_NUMBER);
        return leaseDetails;
    }

    @BeforeEach
    public void initTest() {
        leaseDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaseDetails() throws Exception {
        int databaseSizeBeforeCreate = leaseDetailsRepository.findAll().size();
        // Create the LeaseDetails
        LeaseDetailsDTO leaseDetailsDTO = leaseDetailsMapper.toDto(leaseDetails);
        restLeaseDetailsMockMvc.perform(post("/api/lease-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the LeaseDetails in the database
        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseDetails testLeaseDetails = leaseDetailsList.get(leaseDetailsList.size() - 1);
        assertThat(testLeaseDetails.getLeaseContractNumber()).isEqualTo(DEFAULT_LEASE_CONTRACT_NUMBER);
        assertThat(testLeaseDetails.getIncrementalBorrowingRate()).isEqualTo(DEFAULT_INCREMENTAL_BORROWING_RATE);
        assertThat(testLeaseDetails.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testLeaseDetails.getLeasePrepayments()).isEqualTo(DEFAULT_LEASE_PREPAYMENTS);
        assertThat(testLeaseDetails.getInitialDirectCosts()).isEqualTo(DEFAULT_INITIAL_DIRECT_COSTS);
        assertThat(testLeaseDetails.getDemolitionCosts()).isEqualTo(DEFAULT_DEMOLITION_COSTS);
        assertThat(testLeaseDetails.getAssetAccountNumber()).isEqualTo(DEFAULT_ASSET_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getLiabilityAccountNumber()).isEqualTo(DEFAULT_LIABILITY_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getDepreciationAccountNumber()).isEqualTo(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getInterestAccountNumber()).isEqualTo(DEFAULT_INTEREST_ACCOUNT_NUMBER);

        // Validate the LeaseDetails in Elasticsearch
        verify(mockLeaseDetailsSearchRepository, times(1)).save(testLeaseDetails);
    }

    @Test
    @Transactional
    public void createLeaseDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaseDetailsRepository.findAll().size();

        // Create the LeaseDetails with an existing ID
        leaseDetails.setId(1L);
        LeaseDetailsDTO leaseDetailsDTO = leaseDetailsMapper.toDto(leaseDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseDetailsMockMvc.perform(post("/api/lease-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaseDetails in the database
        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseDetails in Elasticsearch
        verify(mockLeaseDetailsSearchRepository, times(0)).save(leaseDetails);
    }


    @Test
    @Transactional
    public void checkLeaseContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseDetailsRepository.findAll().size();
        // set the field null
        leaseDetails.setLeaseContractNumber(null);

        // Create the LeaseDetails, which fails.
        LeaseDetailsDTO leaseDetailsDTO = leaseDetailsMapper.toDto(leaseDetails);


        restLeaseDetailsMockMvc.perform(post("/api/lease-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDetailsDTO)))
            .andExpect(status().isBadRequest());

        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaseDetails() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList
        restLeaseDetailsMockMvc.perform(get("/api/lease-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].incrementalBorrowingRate").value(hasItem(DEFAULT_INCREMENTAL_BORROWING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].leasePrepayments").value(hasItem(DEFAULT_LEASE_PREPAYMENTS.intValue())))
            .andExpect(jsonPath("$.[*].initialDirectCosts").value(hasItem(DEFAULT_INITIAL_DIRECT_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].demolitionCosts").value(hasItem(DEFAULT_DEMOLITION_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestAccountNumber").value(hasItem(DEFAULT_INTEREST_ACCOUNT_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getLeaseDetails() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get the leaseDetails
        restLeaseDetailsMockMvc.perform(get("/api/lease-details/{id}", leaseDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseDetails.getId().intValue()))
            .andExpect(jsonPath("$.leaseContractNumber").value(DEFAULT_LEASE_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.incrementalBorrowingRate").value(DEFAULT_INCREMENTAL_BORROWING_RATE.doubleValue()))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.leasePrepayments").value(DEFAULT_LEASE_PREPAYMENTS.intValue()))
            .andExpect(jsonPath("$.initialDirectCosts").value(DEFAULT_INITIAL_DIRECT_COSTS.intValue()))
            .andExpect(jsonPath("$.demolitionCosts").value(DEFAULT_DEMOLITION_COSTS.intValue()))
            .andExpect(jsonPath("$.assetAccountNumber").value(DEFAULT_ASSET_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.liabilityAccountNumber").value(DEFAULT_LIABILITY_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.depreciationAccountNumber").value(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.interestAccountNumber").value(DEFAULT_INTEREST_ACCOUNT_NUMBER));
    }


    @Test
    @Transactional
    public void getLeaseDetailsByIdFiltering() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        Long id = leaseDetails.getId();

        defaultLeaseDetailsShouldBeFound("id.equals=" + id);
        defaultLeaseDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseDetailsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.equals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the leaseDetailsList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.equals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber not equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.notEquals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the leaseDetailsList where leaseContractNumber not equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.notEquals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber in DEFAULT_LEASE_CONTRACT_NUMBER or UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.in=" + DEFAULT_LEASE_CONTRACT_NUMBER + "," + UPDATED_LEASE_CONTRACT_NUMBER);

        // Get all the leaseDetailsList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.in=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber is not null
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.specified=true");

        // Get all the leaseDetailsList where leaseContractNumber is null
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber contains DEFAULT_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.contains=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the leaseDetailsList where leaseContractNumber contains UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.contains=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeaseContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leaseContractNumber does not contain DEFAULT_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("leaseContractNumber.doesNotContain=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the leaseDetailsList where leaseContractNumber does not contain UPDATED_LEASE_CONTRACT_NUMBER
        defaultLeaseDetailsShouldBeFound("leaseContractNumber.doesNotContain=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate equals to DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.equals=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate equals to UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.equals=" + UPDATED_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate not equals to DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.notEquals=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate not equals to UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.notEquals=" + UPDATED_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate in DEFAULT_INCREMENTAL_BORROWING_RATE or UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.in=" + DEFAULT_INCREMENTAL_BORROWING_RATE + "," + UPDATED_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate equals to UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.in=" + UPDATED_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate is not null
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.specified=true");

        // Get all the leaseDetailsList where incrementalBorrowingRate is null
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate is greater than or equal to DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.greaterThanOrEqual=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate is greater than or equal to UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.greaterThanOrEqual=" + UPDATED_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate is less than or equal to DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.lessThanOrEqual=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate is less than or equal to SMALLER_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.lessThanOrEqual=" + SMALLER_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate is less than DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.lessThan=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate is less than UPDATED_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.lessThan=" + UPDATED_INCREMENTAL_BORROWING_RATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByIncrementalBorrowingRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where incrementalBorrowingRate is greater than DEFAULT_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldNotBeFound("incrementalBorrowingRate.greaterThan=" + DEFAULT_INCREMENTAL_BORROWING_RATE);

        // Get all the leaseDetailsList where incrementalBorrowingRate is greater than SMALLER_INCREMENTAL_BORROWING_RATE
        defaultLeaseDetailsShouldBeFound("incrementalBorrowingRate.greaterThan=" + SMALLER_INCREMENTAL_BORROWING_RATE);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate is not null
        defaultLeaseDetailsShouldBeFound("commencementDate.specified=true");

        // Get all the leaseDetailsList where commencementDate is null
        defaultLeaseDetailsShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the leaseDetailsList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultLeaseDetailsShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments equals to DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.equals=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments equals to UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.equals=" + UPDATED_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments not equals to DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.notEquals=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments not equals to UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.notEquals=" + UPDATED_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments in DEFAULT_LEASE_PREPAYMENTS or UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.in=" + DEFAULT_LEASE_PREPAYMENTS + "," + UPDATED_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments equals to UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.in=" + UPDATED_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments is not null
        defaultLeaseDetailsShouldBeFound("leasePrepayments.specified=true");

        // Get all the leaseDetailsList where leasePrepayments is null
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments is greater than or equal to DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.greaterThanOrEqual=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments is greater than or equal to UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.greaterThanOrEqual=" + UPDATED_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments is less than or equal to DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.lessThanOrEqual=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments is less than or equal to SMALLER_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.lessThanOrEqual=" + SMALLER_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments is less than DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.lessThan=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments is less than UPDATED_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.lessThan=" + UPDATED_LEASE_PREPAYMENTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLeasePrepaymentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where leasePrepayments is greater than DEFAULT_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldNotBeFound("leasePrepayments.greaterThan=" + DEFAULT_LEASE_PREPAYMENTS);

        // Get all the leaseDetailsList where leasePrepayments is greater than SMALLER_LEASE_PREPAYMENTS
        defaultLeaseDetailsShouldBeFound("leasePrepayments.greaterThan=" + SMALLER_LEASE_PREPAYMENTS);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts equals to DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.equals=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts equals to UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.equals=" + UPDATED_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts not equals to DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.notEquals=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts not equals to UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.notEquals=" + UPDATED_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts in DEFAULT_INITIAL_DIRECT_COSTS or UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.in=" + DEFAULT_INITIAL_DIRECT_COSTS + "," + UPDATED_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts equals to UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.in=" + UPDATED_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts is not null
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.specified=true");

        // Get all the leaseDetailsList where initialDirectCosts is null
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts is greater than or equal to DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.greaterThanOrEqual=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts is greater than or equal to UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.greaterThanOrEqual=" + UPDATED_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts is less than or equal to DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.lessThanOrEqual=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts is less than or equal to SMALLER_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.lessThanOrEqual=" + SMALLER_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts is less than DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.lessThan=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts is less than UPDATED_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.lessThan=" + UPDATED_INITIAL_DIRECT_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInitialDirectCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where initialDirectCosts is greater than DEFAULT_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldNotBeFound("initialDirectCosts.greaterThan=" + DEFAULT_INITIAL_DIRECT_COSTS);

        // Get all the leaseDetailsList where initialDirectCosts is greater than SMALLER_INITIAL_DIRECT_COSTS
        defaultLeaseDetailsShouldBeFound("initialDirectCosts.greaterThan=" + SMALLER_INITIAL_DIRECT_COSTS);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts equals to DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.equals=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts equals to UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.equals=" + UPDATED_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts not equals to DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.notEquals=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts not equals to UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.notEquals=" + UPDATED_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts in DEFAULT_DEMOLITION_COSTS or UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.in=" + DEFAULT_DEMOLITION_COSTS + "," + UPDATED_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts equals to UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.in=" + UPDATED_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts is not null
        defaultLeaseDetailsShouldBeFound("demolitionCosts.specified=true");

        // Get all the leaseDetailsList where demolitionCosts is null
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts is greater than or equal to DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.greaterThanOrEqual=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts is greater than or equal to UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.greaterThanOrEqual=" + UPDATED_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts is less than or equal to DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.lessThanOrEqual=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts is less than or equal to SMALLER_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.lessThanOrEqual=" + SMALLER_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsLessThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts is less than DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.lessThan=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts is less than UPDATED_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.lessThan=" + UPDATED_DEMOLITION_COSTS);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDemolitionCostsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where demolitionCosts is greater than DEFAULT_DEMOLITION_COSTS
        defaultLeaseDetailsShouldNotBeFound("demolitionCosts.greaterThan=" + DEFAULT_DEMOLITION_COSTS);

        // Get all the leaseDetailsList where demolitionCosts is greater than SMALLER_DEMOLITION_COSTS
        defaultLeaseDetailsShouldBeFound("demolitionCosts.greaterThan=" + SMALLER_DEMOLITION_COSTS);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.equals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.equals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber not equals to DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.notEquals=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where assetAccountNumber not equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.notEquals=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber in DEFAULT_ASSET_ACCOUNT_NUMBER or UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.in=" + DEFAULT_ASSET_ACCOUNT_NUMBER + "," + UPDATED_ASSET_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where assetAccountNumber equals to UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.in=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber is not null
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.specified=true");

        // Get all the leaseDetailsList where assetAccountNumber is null
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber contains DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.contains=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where assetAccountNumber contains UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.contains=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByAssetAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where assetAccountNumber does not contain DEFAULT_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("assetAccountNumber.doesNotContain=" + DEFAULT_ASSET_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where assetAccountNumber does not contain UPDATED_ASSET_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("assetAccountNumber.doesNotContain=" + UPDATED_ASSET_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber equals to DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.equals=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where liabilityAccountNumber equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.equals=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber not equals to DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.notEquals=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where liabilityAccountNumber not equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.notEquals=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber in DEFAULT_LIABILITY_ACCOUNT_NUMBER or UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.in=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER + "," + UPDATED_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where liabilityAccountNumber equals to UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.in=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber is not null
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.specified=true");

        // Get all the leaseDetailsList where liabilityAccountNumber is null
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber contains DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.contains=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where liabilityAccountNumber contains UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.contains=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByLiabilityAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where liabilityAccountNumber does not contain DEFAULT_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("liabilityAccountNumber.doesNotContain=" + DEFAULT_LIABILITY_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where liabilityAccountNumber does not contain UPDATED_LIABILITY_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("liabilityAccountNumber.doesNotContain=" + UPDATED_LIABILITY_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.equals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.equals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber not equals to DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.notEquals=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where depreciationAccountNumber not equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.notEquals=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber in DEFAULT_DEPRECIATION_ACCOUNT_NUMBER or UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.in=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER + "," + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where depreciationAccountNumber equals to UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.in=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber is not null
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.specified=true");

        // Get all the leaseDetailsList where depreciationAccountNumber is null
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber contains DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.contains=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where depreciationAccountNumber contains UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.contains=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByDepreciationAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where depreciationAccountNumber does not contain DEFAULT_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("depreciationAccountNumber.doesNotContain=" + DEFAULT_DEPRECIATION_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where depreciationAccountNumber does not contain UPDATED_DEPRECIATION_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("depreciationAccountNumber.doesNotContain=" + UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber equals to DEFAULT_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.equals=" + DEFAULT_INTEREST_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where interestAccountNumber equals to UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.equals=" + UPDATED_INTEREST_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber not equals to DEFAULT_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.notEquals=" + DEFAULT_INTEREST_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where interestAccountNumber not equals to UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.notEquals=" + UPDATED_INTEREST_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber in DEFAULT_INTEREST_ACCOUNT_NUMBER or UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.in=" + DEFAULT_INTEREST_ACCOUNT_NUMBER + "," + UPDATED_INTEREST_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where interestAccountNumber equals to UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.in=" + UPDATED_INTEREST_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber is not null
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.specified=true");

        // Get all the leaseDetailsList where interestAccountNumber is null
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber contains DEFAULT_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.contains=" + DEFAULT_INTEREST_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where interestAccountNumber contains UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.contains=" + UPDATED_INTEREST_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLeaseDetailsByInterestAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        // Get all the leaseDetailsList where interestAccountNumber does not contain DEFAULT_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldNotBeFound("interestAccountNumber.doesNotContain=" + DEFAULT_INTEREST_ACCOUNT_NUMBER);

        // Get all the leaseDetailsList where interestAccountNumber does not contain UPDATED_INTEREST_ACCOUNT_NUMBER
        defaultLeaseDetailsShouldBeFound("interestAccountNumber.doesNotContain=" + UPDATED_INTEREST_ACCOUNT_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseDetailsShouldBeFound(String filter) throws Exception {
        restLeaseDetailsMockMvc.perform(get("/api/lease-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].incrementalBorrowingRate").value(hasItem(DEFAULT_INCREMENTAL_BORROWING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].leasePrepayments").value(hasItem(DEFAULT_LEASE_PREPAYMENTS.intValue())))
            .andExpect(jsonPath("$.[*].initialDirectCosts").value(hasItem(DEFAULT_INITIAL_DIRECT_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].demolitionCosts").value(hasItem(DEFAULT_DEMOLITION_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestAccountNumber").value(hasItem(DEFAULT_INTEREST_ACCOUNT_NUMBER)));

        // Check, that the count call also returns 1
        restLeaseDetailsMockMvc.perform(get("/api/lease-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseDetailsShouldNotBeFound(String filter) throws Exception {
        restLeaseDetailsMockMvc.perform(get("/api/lease-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseDetailsMockMvc.perform(get("/api/lease-details/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeaseDetails() throws Exception {
        // Get the leaseDetails
        restLeaseDetailsMockMvc.perform(get("/api/lease-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaseDetails() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        int databaseSizeBeforeUpdate = leaseDetailsRepository.findAll().size();

        // Update the leaseDetails
        LeaseDetails updatedLeaseDetails = leaseDetailsRepository.findById(leaseDetails.getId()).get();
        // Disconnect from session so that the updates on updatedLeaseDetails are not directly saved in db
        em.detach(updatedLeaseDetails);
        updatedLeaseDetails
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .incrementalBorrowingRate(UPDATED_INCREMENTAL_BORROWING_RATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .leasePrepayments(UPDATED_LEASE_PREPAYMENTS)
            .initialDirectCosts(UPDATED_INITIAL_DIRECT_COSTS)
            .demolitionCosts(UPDATED_DEMOLITION_COSTS)
            .assetAccountNumber(UPDATED_ASSET_ACCOUNT_NUMBER)
            .liabilityAccountNumber(UPDATED_LIABILITY_ACCOUNT_NUMBER)
            .depreciationAccountNumber(UPDATED_DEPRECIATION_ACCOUNT_NUMBER)
            .interestAccountNumber(UPDATED_INTEREST_ACCOUNT_NUMBER);
        LeaseDetailsDTO leaseDetailsDTO = leaseDetailsMapper.toDto(updatedLeaseDetails);

        restLeaseDetailsMockMvc.perform(put("/api/lease-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the LeaseDetails in the database
        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeUpdate);
        LeaseDetails testLeaseDetails = leaseDetailsList.get(leaseDetailsList.size() - 1);
        assertThat(testLeaseDetails.getLeaseContractNumber()).isEqualTo(UPDATED_LEASE_CONTRACT_NUMBER);
        assertThat(testLeaseDetails.getIncrementalBorrowingRate()).isEqualTo(UPDATED_INCREMENTAL_BORROWING_RATE);
        assertThat(testLeaseDetails.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testLeaseDetails.getLeasePrepayments()).isEqualTo(UPDATED_LEASE_PREPAYMENTS);
        assertThat(testLeaseDetails.getInitialDirectCosts()).isEqualTo(UPDATED_INITIAL_DIRECT_COSTS);
        assertThat(testLeaseDetails.getDemolitionCosts()).isEqualTo(UPDATED_DEMOLITION_COSTS);
        assertThat(testLeaseDetails.getAssetAccountNumber()).isEqualTo(UPDATED_ASSET_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getLiabilityAccountNumber()).isEqualTo(UPDATED_LIABILITY_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getDepreciationAccountNumber()).isEqualTo(UPDATED_DEPRECIATION_ACCOUNT_NUMBER);
        assertThat(testLeaseDetails.getInterestAccountNumber()).isEqualTo(UPDATED_INTEREST_ACCOUNT_NUMBER);

        // Validate the LeaseDetails in Elasticsearch
        verify(mockLeaseDetailsSearchRepository, times(1)).save(testLeaseDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaseDetails() throws Exception {
        int databaseSizeBeforeUpdate = leaseDetailsRepository.findAll().size();

        // Create the LeaseDetails
        LeaseDetailsDTO leaseDetailsDTO = leaseDetailsMapper.toDto(leaseDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseDetailsMockMvc.perform(put("/api/lease-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leaseDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaseDetails in the database
        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseDetails in Elasticsearch
        verify(mockLeaseDetailsSearchRepository, times(0)).save(leaseDetails);
    }

    @Test
    @Transactional
    public void deleteLeaseDetails() throws Exception {
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);

        int databaseSizeBeforeDelete = leaseDetailsRepository.findAll().size();

        // Delete the leaseDetails
        restLeaseDetailsMockMvc.perform(delete("/api/lease-details/{id}", leaseDetails.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseDetails> leaseDetailsList = leaseDetailsRepository.findAll();
        assertThat(leaseDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseDetails in Elasticsearch
        verify(mockLeaseDetailsSearchRepository, times(1)).deleteById(leaseDetails.getId());
    }

    @Test
    @Transactional
    public void searchLeaseDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseDetailsRepository.saveAndFlush(leaseDetails);
        when(mockLeaseDetailsSearchRepository.search(queryStringQuery("id:" + leaseDetails.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseDetails), PageRequest.of(0, 1), 1));

        // Search the leaseDetails
        restLeaseDetailsMockMvc.perform(get("/api/_search/lease-details?query=id:" + leaseDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].incrementalBorrowingRate").value(hasItem(DEFAULT_INCREMENTAL_BORROWING_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].leasePrepayments").value(hasItem(DEFAULT_LEASE_PREPAYMENTS.intValue())))
            .andExpect(jsonPath("$.[*].initialDirectCosts").value(hasItem(DEFAULT_INITIAL_DIRECT_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].demolitionCosts").value(hasItem(DEFAULT_DEMOLITION_COSTS.intValue())))
            .andExpect(jsonPath("$.[*].assetAccountNumber").value(hasItem(DEFAULT_ASSET_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].liabilityAccountNumber").value(hasItem(DEFAULT_LIABILITY_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].depreciationAccountNumber").value(hasItem(DEFAULT_DEPRECIATION_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].interestAccountNumber").value(hasItem(DEFAULT_INTEREST_ACCOUNT_NUMBER)));
    }
}
