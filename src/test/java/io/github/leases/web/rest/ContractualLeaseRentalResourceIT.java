package io.github.leases.web.rest;

import io.github.leases.LeasesApp;
import io.github.leases.domain.ContractualLeaseRental;
import io.github.leases.repository.ContractualLeaseRentalRepository;
import io.github.leases.repository.search.ContractualLeaseRentalSearchRepository;
import io.github.leases.service.ContractualLeaseRentalService;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import io.github.leases.service.mapper.ContractualLeaseRentalMapper;
import io.github.leases.service.dto.ContractualLeaseRentalCriteria;
import io.github.leases.service.ContractualLeaseRentalQueryService;

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
 * Integration tests for the {@link ContractualLeaseRentalResource} REST controller.
 */
@SpringBootTest(classes = LeasesApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ContractualLeaseRentalResourceIT {

    private static final String DEFAULT_LEASE_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_RENTAL_SEQUENCE_NUMBER = 1;
    private static final Integer UPDATED_RENTAL_SEQUENCE_NUMBER = 2;
    private static final Integer SMALLER_RENTAL_SEQUENCE_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_LEASE_RENTAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEASE_RENTAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LEASE_RENTAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LEASE_RENTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_LEASE_RENTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_LEASE_RENTAL_AMOUNT = new BigDecimal(1 - 1);

    @Autowired
    private ContractualLeaseRentalRepository contractualLeaseRentalRepository;

    @Autowired
    private ContractualLeaseRentalMapper contractualLeaseRentalMapper;

    @Autowired
    private ContractualLeaseRentalService contractualLeaseRentalService;

    /**
     * This repository is mocked in the io.github.leases.repository.search test package.
     *
     * @see io.github.leases.repository.search.ContractualLeaseRentalSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContractualLeaseRentalSearchRepository mockContractualLeaseRentalSearchRepository;

    @Autowired
    private ContractualLeaseRentalQueryService contractualLeaseRentalQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractualLeaseRentalMockMvc;

    private ContractualLeaseRental contractualLeaseRental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractualLeaseRental createEntity(EntityManager em) {
        ContractualLeaseRental contractualLeaseRental = new ContractualLeaseRental()
            .leaseContractNumber(DEFAULT_LEASE_CONTRACT_NUMBER)
            .rentalSequenceNumber(DEFAULT_RENTAL_SEQUENCE_NUMBER)
            .leaseRentalDate(DEFAULT_LEASE_RENTAL_DATE)
            .leaseRentalAmount(DEFAULT_LEASE_RENTAL_AMOUNT);
        return contractualLeaseRental;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractualLeaseRental createUpdatedEntity(EntityManager em) {
        ContractualLeaseRental contractualLeaseRental = new ContractualLeaseRental()
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .rentalSequenceNumber(UPDATED_RENTAL_SEQUENCE_NUMBER)
            .leaseRentalDate(UPDATED_LEASE_RENTAL_DATE)
            .leaseRentalAmount(UPDATED_LEASE_RENTAL_AMOUNT);
        return contractualLeaseRental;
    }

    @BeforeEach
    public void initTest() {
        contractualLeaseRental = createEntity(em);
    }

    @Test
    @Transactional
    public void createContractualLeaseRental() throws Exception {
        int databaseSizeBeforeCreate = contractualLeaseRentalRepository.findAll().size();
        // Create the ContractualLeaseRental
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(contractualLeaseRental);
        restContractualLeaseRentalMockMvc.perform(post("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isCreated());

        // Validate the ContractualLeaseRental in the database
        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeCreate + 1);
        ContractualLeaseRental testContractualLeaseRental = contractualLeaseRentalList.get(contractualLeaseRentalList.size() - 1);
        assertThat(testContractualLeaseRental.getLeaseContractNumber()).isEqualTo(DEFAULT_LEASE_CONTRACT_NUMBER);
        assertThat(testContractualLeaseRental.getRentalSequenceNumber()).isEqualTo(DEFAULT_RENTAL_SEQUENCE_NUMBER);
        assertThat(testContractualLeaseRental.getLeaseRentalDate()).isEqualTo(DEFAULT_LEASE_RENTAL_DATE);
        assertThat(testContractualLeaseRental.getLeaseRentalAmount()).isEqualTo(DEFAULT_LEASE_RENTAL_AMOUNT);

        // Validate the ContractualLeaseRental in Elasticsearch
        verify(mockContractualLeaseRentalSearchRepository, times(1)).save(testContractualLeaseRental);
    }

    @Test
    @Transactional
    public void createContractualLeaseRentalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractualLeaseRentalRepository.findAll().size();

        // Create the ContractualLeaseRental with an existing ID
        contractualLeaseRental.setId(1L);
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(contractualLeaseRental);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractualLeaseRentalMockMvc.perform(post("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContractualLeaseRental in the database
        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContractualLeaseRental in Elasticsearch
        verify(mockContractualLeaseRentalSearchRepository, times(0)).save(contractualLeaseRental);
    }


    @Test
    @Transactional
    public void checkLeaseContractNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractualLeaseRentalRepository.findAll().size();
        // set the field null
        contractualLeaseRental.setLeaseContractNumber(null);

        // Create the ContractualLeaseRental, which fails.
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(contractualLeaseRental);


        restContractualLeaseRentalMockMvc.perform(post("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isBadRequest());

        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentalSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractualLeaseRentalRepository.findAll().size();
        // set the field null
        contractualLeaseRental.setRentalSequenceNumber(null);

        // Create the ContractualLeaseRental, which fails.
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(contractualLeaseRental);


        restContractualLeaseRentalMockMvc.perform(post("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isBadRequest());

        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentals() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractualLeaseRental.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].rentalSequenceNumber").value(hasItem(DEFAULT_RENTAL_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseRentalDate").value(hasItem(DEFAULT_LEASE_RENTAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseRentalAmount").value(hasItem(DEFAULT_LEASE_RENTAL_AMOUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getContractualLeaseRental() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get the contractualLeaseRental
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals/{id}", contractualLeaseRental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractualLeaseRental.getId().intValue()))
            .andExpect(jsonPath("$.leaseContractNumber").value(DEFAULT_LEASE_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.rentalSequenceNumber").value(DEFAULT_RENTAL_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.leaseRentalDate").value(DEFAULT_LEASE_RENTAL_DATE.toString()))
            .andExpect(jsonPath("$.leaseRentalAmount").value(DEFAULT_LEASE_RENTAL_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getContractualLeaseRentalsByIdFiltering() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        Long id = contractualLeaseRental.getId();

        defaultContractualLeaseRentalShouldBeFound("id.equals=" + id);
        defaultContractualLeaseRentalShouldNotBeFound("id.notEquals=" + id);

        defaultContractualLeaseRentalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractualLeaseRentalShouldNotBeFound("id.greaterThan=" + id);

        defaultContractualLeaseRentalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractualLeaseRentalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.equals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the contractualLeaseRentalList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.equals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber not equals to DEFAULT_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.notEquals=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the contractualLeaseRentalList where leaseContractNumber not equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.notEquals=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber in DEFAULT_LEASE_CONTRACT_NUMBER or UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.in=" + DEFAULT_LEASE_CONTRACT_NUMBER + "," + UPDATED_LEASE_CONTRACT_NUMBER);

        // Get all the contractualLeaseRentalList where leaseContractNumber equals to UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.in=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber is not null
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.specified=true");

        // Get all the contractualLeaseRentalList where leaseContractNumber is null
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberContainsSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber contains DEFAULT_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.contains=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the contractualLeaseRentalList where leaseContractNumber contains UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.contains=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseContractNumber does not contain DEFAULT_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("leaseContractNumber.doesNotContain=" + DEFAULT_LEASE_CONTRACT_NUMBER);

        // Get all the contractualLeaseRentalList where leaseContractNumber does not contain UPDATED_LEASE_CONTRACT_NUMBER
        defaultContractualLeaseRentalShouldBeFound("leaseContractNumber.doesNotContain=" + UPDATED_LEASE_CONTRACT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber equals to DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.equals=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber equals to UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.equals=" + UPDATED_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber not equals to DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.notEquals=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber not equals to UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.notEquals=" + UPDATED_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber in DEFAULT_RENTAL_SEQUENCE_NUMBER or UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.in=" + DEFAULT_RENTAL_SEQUENCE_NUMBER + "," + UPDATED_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber equals to UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.in=" + UPDATED_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is not null
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.specified=true");

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is null
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is greater than or equal to DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.greaterThanOrEqual=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is greater than or equal to UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.greaterThanOrEqual=" + UPDATED_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is less than or equal to DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.lessThanOrEqual=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is less than or equal to SMALLER_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.lessThanOrEqual=" + SMALLER_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is less than DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.lessThan=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is less than UPDATED_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.lessThan=" + UPDATED_RENTAL_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByRentalSequenceNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is greater than DEFAULT_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldNotBeFound("rentalSequenceNumber.greaterThan=" + DEFAULT_RENTAL_SEQUENCE_NUMBER);

        // Get all the contractualLeaseRentalList where rentalSequenceNumber is greater than SMALLER_RENTAL_SEQUENCE_NUMBER
        defaultContractualLeaseRentalShouldBeFound("rentalSequenceNumber.greaterThan=" + SMALLER_RENTAL_SEQUENCE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate equals to DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.equals=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate equals to UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.equals=" + UPDATED_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate not equals to DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.notEquals=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate not equals to UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.notEquals=" + UPDATED_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate in DEFAULT_LEASE_RENTAL_DATE or UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.in=" + DEFAULT_LEASE_RENTAL_DATE + "," + UPDATED_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate equals to UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.in=" + UPDATED_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate is not null
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.specified=true");

        // Get all the contractualLeaseRentalList where leaseRentalDate is null
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate is greater than or equal to DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.greaterThanOrEqual=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate is greater than or equal to UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.greaterThanOrEqual=" + UPDATED_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate is less than or equal to DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.lessThanOrEqual=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate is less than or equal to SMALLER_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.lessThanOrEqual=" + SMALLER_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate is less than DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.lessThan=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate is less than UPDATED_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.lessThan=" + UPDATED_LEASE_RENTAL_DATE);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalDate is greater than DEFAULT_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalDate.greaterThan=" + DEFAULT_LEASE_RENTAL_DATE);

        // Get all the contractualLeaseRentalList where leaseRentalDate is greater than SMALLER_LEASE_RENTAL_DATE
        defaultContractualLeaseRentalShouldBeFound("leaseRentalDate.greaterThan=" + SMALLER_LEASE_RENTAL_DATE);
    }


    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount equals to DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.equals=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount equals to UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.equals=" + UPDATED_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount not equals to DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.notEquals=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount not equals to UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.notEquals=" + UPDATED_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount in DEFAULT_LEASE_RENTAL_AMOUNT or UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.in=" + DEFAULT_LEASE_RENTAL_AMOUNT + "," + UPDATED_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount equals to UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.in=" + UPDATED_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is not null
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.specified=true");

        // Get all the contractualLeaseRentalList where leaseRentalAmount is null
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is greater than or equal to DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.greaterThanOrEqual=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is greater than or equal to UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.greaterThanOrEqual=" + UPDATED_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is less than or equal to DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.lessThanOrEqual=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is less than or equal to SMALLER_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.lessThanOrEqual=" + SMALLER_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is less than DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.lessThan=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is less than UPDATED_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.lessThan=" + UPDATED_LEASE_RENTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContractualLeaseRentalsByLeaseRentalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is greater than DEFAULT_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldNotBeFound("leaseRentalAmount.greaterThan=" + DEFAULT_LEASE_RENTAL_AMOUNT);

        // Get all the contractualLeaseRentalList where leaseRentalAmount is greater than SMALLER_LEASE_RENTAL_AMOUNT
        defaultContractualLeaseRentalShouldBeFound("leaseRentalAmount.greaterThan=" + SMALLER_LEASE_RENTAL_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractualLeaseRentalShouldBeFound(String filter) throws Exception {
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractualLeaseRental.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].rentalSequenceNumber").value(hasItem(DEFAULT_RENTAL_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseRentalDate").value(hasItem(DEFAULT_LEASE_RENTAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseRentalAmount").value(hasItem(DEFAULT_LEASE_RENTAL_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractualLeaseRentalShouldNotBeFound(String filter) throws Exception {
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingContractualLeaseRental() throws Exception {
        // Get the contractualLeaseRental
        restContractualLeaseRentalMockMvc.perform(get("/api/contractual-lease-rentals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractualLeaseRental() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        int databaseSizeBeforeUpdate = contractualLeaseRentalRepository.findAll().size();

        // Update the contractualLeaseRental
        ContractualLeaseRental updatedContractualLeaseRental = contractualLeaseRentalRepository.findById(contractualLeaseRental.getId()).get();
        // Disconnect from session so that the updates on updatedContractualLeaseRental are not directly saved in db
        em.detach(updatedContractualLeaseRental);
        updatedContractualLeaseRental
            .leaseContractNumber(UPDATED_LEASE_CONTRACT_NUMBER)
            .rentalSequenceNumber(UPDATED_RENTAL_SEQUENCE_NUMBER)
            .leaseRentalDate(UPDATED_LEASE_RENTAL_DATE)
            .leaseRentalAmount(UPDATED_LEASE_RENTAL_AMOUNT);
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(updatedContractualLeaseRental);

        restContractualLeaseRentalMockMvc.perform(put("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isOk());

        // Validate the ContractualLeaseRental in the database
        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeUpdate);
        ContractualLeaseRental testContractualLeaseRental = contractualLeaseRentalList.get(contractualLeaseRentalList.size() - 1);
        assertThat(testContractualLeaseRental.getLeaseContractNumber()).isEqualTo(UPDATED_LEASE_CONTRACT_NUMBER);
        assertThat(testContractualLeaseRental.getRentalSequenceNumber()).isEqualTo(UPDATED_RENTAL_SEQUENCE_NUMBER);
        assertThat(testContractualLeaseRental.getLeaseRentalDate()).isEqualTo(UPDATED_LEASE_RENTAL_DATE);
        assertThat(testContractualLeaseRental.getLeaseRentalAmount()).isEqualTo(UPDATED_LEASE_RENTAL_AMOUNT);

        // Validate the ContractualLeaseRental in Elasticsearch
        verify(mockContractualLeaseRentalSearchRepository, times(1)).save(testContractualLeaseRental);
    }

    @Test
    @Transactional
    public void updateNonExistingContractualLeaseRental() throws Exception {
        int databaseSizeBeforeUpdate = contractualLeaseRentalRepository.findAll().size();

        // Create the ContractualLeaseRental
        ContractualLeaseRentalDTO contractualLeaseRentalDTO = contractualLeaseRentalMapper.toDto(contractualLeaseRental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractualLeaseRentalMockMvc.perform(put("/api/contractual-lease-rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contractualLeaseRentalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContractualLeaseRental in the database
        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractualLeaseRental in Elasticsearch
        verify(mockContractualLeaseRentalSearchRepository, times(0)).save(contractualLeaseRental);
    }

    @Test
    @Transactional
    public void deleteContractualLeaseRental() throws Exception {
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);

        int databaseSizeBeforeDelete = contractualLeaseRentalRepository.findAll().size();

        // Delete the contractualLeaseRental
        restContractualLeaseRentalMockMvc.perform(delete("/api/contractual-lease-rentals/{id}", contractualLeaseRental.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractualLeaseRental> contractualLeaseRentalList = contractualLeaseRentalRepository.findAll();
        assertThat(contractualLeaseRentalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContractualLeaseRental in Elasticsearch
        verify(mockContractualLeaseRentalSearchRepository, times(1)).deleteById(contractualLeaseRental.getId());
    }

    @Test
    @Transactional
    public void searchContractualLeaseRental() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contractualLeaseRentalRepository.saveAndFlush(contractualLeaseRental);
        when(mockContractualLeaseRentalSearchRepository.search(queryStringQuery("id:" + contractualLeaseRental.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contractualLeaseRental), PageRequest.of(0, 1), 1));

        // Search the contractualLeaseRental
        restContractualLeaseRentalMockMvc.perform(get("/api/_search/contractual-lease-rentals?query=id:" + contractualLeaseRental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractualLeaseRental.getId().intValue())))
            .andExpect(jsonPath("$.[*].leaseContractNumber").value(hasItem(DEFAULT_LEASE_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].rentalSequenceNumber").value(hasItem(DEFAULT_RENTAL_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].leaseRentalDate").value(hasItem(DEFAULT_LEASE_RENTAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaseRentalAmount").value(hasItem(DEFAULT_LEASE_RENTAL_AMOUNT.intValue())));
    }
}
