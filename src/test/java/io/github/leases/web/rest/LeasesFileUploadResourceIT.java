package io.github.leases.web.rest;

import io.github.leases.LeasesApp;
import io.github.leases.domain.LeasesFileUpload;
import io.github.leases.repository.LeasesFileUploadRepository;
import io.github.leases.repository.search.LeasesFileUploadSearchRepository;
import io.github.leases.service.LeasesFileUploadService;
import io.github.leases.service.dto.LeasesFileUploadDTO;
import io.github.leases.service.mapper.LeasesFileUploadMapper;
import io.github.leases.service.dto.LeasesFileUploadCriteria;
import io.github.leases.service.LeasesFileUploadQueryService;

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
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
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
 * Integration tests for the {@link LeasesFileUploadResource} REST controller.
 */
@SpringBootTest(classes = LeasesApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeasesFileUploadResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_TO = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_LEASES_FILE_TYPE_ID = 1L;
    private static final Long UPDATED_LEASES_FILE_TYPE_ID = 2L;
    private static final Long SMALLER_LEASES_FILE_TYPE_ID = 1L - 1L;

    private static final byte[] DEFAULT_DATA_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_UPLOAD_SUCCESSFUL = false;
    private static final Boolean UPDATED_UPLOAD_SUCCESSFUL = true;

    private static final Boolean DEFAULT_UPLOAD_PROCESSED = false;
    private static final Boolean UPDATED_UPLOAD_PROCESSED = true;

    private static final String DEFAULT_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_TOKEN = "BBBBBBBBBB";

    @Autowired
    private LeasesFileUploadRepository leasesFileUploadRepository;

    @Autowired
    private LeasesFileUploadMapper leasesFileUploadMapper;

    @Autowired
    private LeasesFileUploadService leasesFileUploadService;

    /**
     * This repository is mocked in the io.github.leases.repository.search test package.
     *
     * @see io.github.leases.repository.search.LeasesFileUploadSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeasesFileUploadSearchRepository mockLeasesFileUploadSearchRepository;

    @Autowired
    private LeasesFileUploadQueryService leasesFileUploadQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeasesFileUploadMockMvc;

    private LeasesFileUpload leasesFileUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesFileUpload createEntity(EntityManager em) {
        LeasesFileUpload leasesFileUpload = new LeasesFileUpload()
            .description(DEFAULT_DESCRIPTION)
            .fileName(DEFAULT_FILE_NAME)
            .periodFrom(DEFAULT_PERIOD_FROM)
            .periodTo(DEFAULT_PERIOD_TO)
            .leasesFileTypeId(DEFAULT_LEASES_FILE_TYPE_ID)
            .dataFile(DEFAULT_DATA_FILE)
            .dataFileContentType(DEFAULT_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(DEFAULT_UPLOAD_SUCCESSFUL)
            .uploadProcessed(DEFAULT_UPLOAD_PROCESSED)
            .uploadToken(DEFAULT_UPLOAD_TOKEN);
        return leasesFileUpload;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesFileUpload createUpdatedEntity(EntityManager em) {
        LeasesFileUpload leasesFileUpload = new LeasesFileUpload()
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .leasesFileTypeId(UPDATED_LEASES_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        return leasesFileUpload;
    }

    @BeforeEach
    public void initTest() {
        leasesFileUpload = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeasesFileUpload() throws Exception {
        int databaseSizeBeforeCreate = leasesFileUploadRepository.findAll().size();
        // Create the LeasesFileUpload
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);
        restLeasesFileUploadMockMvc.perform(post("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isCreated());

        // Validate the LeasesFileUpload in the database
        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeCreate + 1);
        LeasesFileUpload testLeasesFileUpload = leasesFileUploadList.get(leasesFileUploadList.size() - 1);
        assertThat(testLeasesFileUpload.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeasesFileUpload.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testLeasesFileUpload.getPeriodFrom()).isEqualTo(DEFAULT_PERIOD_FROM);
        assertThat(testLeasesFileUpload.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testLeasesFileUpload.getLeasesFileTypeId()).isEqualTo(DEFAULT_LEASES_FILE_TYPE_ID);
        assertThat(testLeasesFileUpload.getDataFile()).isEqualTo(DEFAULT_DATA_FILE);
        assertThat(testLeasesFileUpload.getDataFileContentType()).isEqualTo(DEFAULT_DATA_FILE_CONTENT_TYPE);
        assertThat(testLeasesFileUpload.isUploadSuccessful()).isEqualTo(DEFAULT_UPLOAD_SUCCESSFUL);
        assertThat(testLeasesFileUpload.isUploadProcessed()).isEqualTo(DEFAULT_UPLOAD_PROCESSED);
        assertThat(testLeasesFileUpload.getUploadToken()).isEqualTo(DEFAULT_UPLOAD_TOKEN);

        // Validate the LeasesFileUpload in Elasticsearch
        verify(mockLeasesFileUploadSearchRepository, times(1)).save(testLeasesFileUpload);
    }

    @Test
    @Transactional
    public void createLeasesFileUploadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leasesFileUploadRepository.findAll().size();

        // Create the LeasesFileUpload with an existing ID
        leasesFileUpload.setId(1L);
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeasesFileUploadMockMvc.perform(post("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesFileUpload in the database
        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeasesFileUpload in Elasticsearch
        verify(mockLeasesFileUploadSearchRepository, times(0)).save(leasesFileUpload);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesFileUploadRepository.findAll().size();
        // set the field null
        leasesFileUpload.setDescription(null);

        // Create the LeasesFileUpload, which fails.
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);


        restLeasesFileUploadMockMvc.perform(post("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesFileUploadRepository.findAll().size();
        // set the field null
        leasesFileUpload.setFileName(null);

        // Create the LeasesFileUpload, which fails.
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);


        restLeasesFileUploadMockMvc.perform(post("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLeasesFileTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesFileUploadRepository.findAll().size();
        // set the field null
        leasesFileUpload.setLeasesFileTypeId(null);

        // Create the LeasesFileUpload, which fails.
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);


        restLeasesFileUploadMockMvc.perform(post("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploads() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].leasesFileTypeId").value(hasItem(DEFAULT_LEASES_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }
    
    @Test
    @Transactional
    public void getLeasesFileUpload() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get the leasesFileUpload
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads/{id}", leasesFileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leasesFileUpload.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.leasesFileTypeId").value(DEFAULT_LEASES_FILE_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.dataFileContentType").value(DEFAULT_DATA_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.dataFile").value(Base64Utils.encodeToString(DEFAULT_DATA_FILE)))
            .andExpect(jsonPath("$.uploadSuccessful").value(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue()))
            .andExpect(jsonPath("$.uploadProcessed").value(DEFAULT_UPLOAD_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.uploadToken").value(DEFAULT_UPLOAD_TOKEN));
    }


    @Test
    @Transactional
    public void getLeasesFileUploadsByIdFiltering() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        Long id = leasesFileUpload.getId();

        defaultLeasesFileUploadShouldBeFound("id.equals=" + id);
        defaultLeasesFileUploadShouldNotBeFound("id.notEquals=" + id);

        defaultLeasesFileUploadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeasesFileUploadShouldNotBeFound("id.greaterThan=" + id);

        defaultLeasesFileUploadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeasesFileUploadShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description equals to DEFAULT_DESCRIPTION
        defaultLeasesFileUploadShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileUploadList where description equals to UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description not equals to DEFAULT_DESCRIPTION
        defaultLeasesFileUploadShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileUploadList where description not equals to UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leasesFileUploadList where description equals to UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description is not null
        defaultLeasesFileUploadShouldBeFound("description.specified=true");

        // Get all the leasesFileUploadList where description is null
        defaultLeasesFileUploadShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description contains DEFAULT_DESCRIPTION
        defaultLeasesFileUploadShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileUploadList where description contains UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where description does not contain DEFAULT_DESCRIPTION
        defaultLeasesFileUploadShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileUploadList where description does not contain UPDATED_DESCRIPTION
        defaultLeasesFileUploadShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName equals to DEFAULT_FILE_NAME
        defaultLeasesFileUploadShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the leasesFileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName not equals to DEFAULT_FILE_NAME
        defaultLeasesFileUploadShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the leasesFileUploadList where fileName not equals to UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the leasesFileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName is not null
        defaultLeasesFileUploadShouldBeFound("fileName.specified=true");

        // Get all the leasesFileUploadList where fileName is null
        defaultLeasesFileUploadShouldNotBeFound("fileName.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName contains DEFAULT_FILE_NAME
        defaultLeasesFileUploadShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the leasesFileUploadList where fileName contains UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where fileName does not contain DEFAULT_FILE_NAME
        defaultLeasesFileUploadShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the leasesFileUploadList where fileName does not contain UPDATED_FILE_NAME
        defaultLeasesFileUploadShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom equals to DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.equals=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.equals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom not equals to DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.notEquals=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom not equals to UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.notEquals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom in DEFAULT_PERIOD_FROM or UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.in=" + DEFAULT_PERIOD_FROM + "," + UPDATED_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.in=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom is not null
        defaultLeasesFileUploadShouldBeFound("periodFrom.specified=true");

        // Get all the leasesFileUploadList where periodFrom is null
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom is greater than or equal to DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.greaterThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom is greater than or equal to UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.greaterThanOrEqual=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom is less than or equal to DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.lessThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom is less than or equal to SMALLER_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.lessThanOrEqual=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsLessThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom is less than DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.lessThan=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom is less than UPDATED_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.lessThan=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodFrom is greater than DEFAULT_PERIOD_FROM
        defaultLeasesFileUploadShouldNotBeFound("periodFrom.greaterThan=" + DEFAULT_PERIOD_FROM);

        // Get all the leasesFileUploadList where periodFrom is greater than SMALLER_PERIOD_FROM
        defaultLeasesFileUploadShouldBeFound("periodFrom.greaterThan=" + SMALLER_PERIOD_FROM);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo equals to DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.equals=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.equals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo not equals to DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.notEquals=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo not equals to UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.notEquals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo in DEFAULT_PERIOD_TO or UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.in=" + DEFAULT_PERIOD_TO + "," + UPDATED_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.in=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo is not null
        defaultLeasesFileUploadShouldBeFound("periodTo.specified=true");

        // Get all the leasesFileUploadList where periodTo is null
        defaultLeasesFileUploadShouldNotBeFound("periodTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo is greater than or equal to DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.greaterThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo is greater than or equal to UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.greaterThanOrEqual=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo is less than or equal to DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.lessThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo is less than or equal to SMALLER_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.lessThanOrEqual=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsLessThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo is less than DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.lessThan=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo is less than UPDATED_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.lessThan=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByPeriodToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where periodTo is greater than DEFAULT_PERIOD_TO
        defaultLeasesFileUploadShouldNotBeFound("periodTo.greaterThan=" + DEFAULT_PERIOD_TO);

        // Get all the leasesFileUploadList where periodTo is greater than SMALLER_PERIOD_TO
        defaultLeasesFileUploadShouldBeFound("periodTo.greaterThan=" + SMALLER_PERIOD_TO);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId equals to DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.equals=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId equals to UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.equals=" + UPDATED_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId not equals to DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.notEquals=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId not equals to UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.notEquals=" + UPDATED_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId in DEFAULT_LEASES_FILE_TYPE_ID or UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.in=" + DEFAULT_LEASES_FILE_TYPE_ID + "," + UPDATED_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId equals to UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.in=" + UPDATED_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId is not null
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.specified=true");

        // Get all the leasesFileUploadList where leasesFileTypeId is null
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId is greater than or equal to DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.greaterThanOrEqual=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId is greater than or equal to UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.greaterThanOrEqual=" + UPDATED_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId is less than or equal to DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.lessThanOrEqual=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId is less than or equal to SMALLER_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.lessThanOrEqual=" + SMALLER_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId is less than DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.lessThan=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId is less than UPDATED_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.lessThan=" + UPDATED_LEASES_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByLeasesFileTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where leasesFileTypeId is greater than DEFAULT_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldNotBeFound("leasesFileTypeId.greaterThan=" + DEFAULT_LEASES_FILE_TYPE_ID);

        // Get all the leasesFileUploadList where leasesFileTypeId is greater than SMALLER_LEASES_FILE_TYPE_ID
        defaultLeasesFileUploadShouldBeFound("leasesFileTypeId.greaterThan=" + SMALLER_LEASES_FILE_TYPE_ID);
    }


    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadSuccessfulIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadSuccessful equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldBeFound("uploadSuccessful.equals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the leasesFileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldNotBeFound("uploadSuccessful.equals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadSuccessfulIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadSuccessful not equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldNotBeFound("uploadSuccessful.notEquals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the leasesFileUploadList where uploadSuccessful not equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldBeFound("uploadSuccessful.notEquals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadSuccessfulIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadSuccessful in DEFAULT_UPLOAD_SUCCESSFUL or UPDATED_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldBeFound("uploadSuccessful.in=" + DEFAULT_UPLOAD_SUCCESSFUL + "," + UPDATED_UPLOAD_SUCCESSFUL);

        // Get all the leasesFileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultLeasesFileUploadShouldNotBeFound("uploadSuccessful.in=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadSuccessfulIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadSuccessful is not null
        defaultLeasesFileUploadShouldBeFound("uploadSuccessful.specified=true");

        // Get all the leasesFileUploadList where uploadSuccessful is null
        defaultLeasesFileUploadShouldNotBeFound("uploadSuccessful.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadProcessed equals to DEFAULT_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldBeFound("uploadProcessed.equals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the leasesFileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldNotBeFound("uploadProcessed.equals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadProcessed not equals to DEFAULT_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldNotBeFound("uploadProcessed.notEquals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the leasesFileUploadList where uploadProcessed not equals to UPDATED_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldBeFound("uploadProcessed.notEquals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadProcessed in DEFAULT_UPLOAD_PROCESSED or UPDATED_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldBeFound("uploadProcessed.in=" + DEFAULT_UPLOAD_PROCESSED + "," + UPDATED_UPLOAD_PROCESSED);

        // Get all the leasesFileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultLeasesFileUploadShouldNotBeFound("uploadProcessed.in=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadProcessed is not null
        defaultLeasesFileUploadShouldBeFound("uploadProcessed.specified=true");

        // Get all the leasesFileUploadList where uploadProcessed is null
        defaultLeasesFileUploadShouldNotBeFound("uploadProcessed.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken equals to DEFAULT_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldBeFound("uploadToken.equals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the leasesFileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.equals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken not equals to DEFAULT_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.notEquals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the leasesFileUploadList where uploadToken not equals to UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldBeFound("uploadToken.notEquals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken in DEFAULT_UPLOAD_TOKEN or UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldBeFound("uploadToken.in=" + DEFAULT_UPLOAD_TOKEN + "," + UPDATED_UPLOAD_TOKEN);

        // Get all the leasesFileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.in=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken is not null
        defaultLeasesFileUploadShouldBeFound("uploadToken.specified=true");

        // Get all the leasesFileUploadList where uploadToken is null
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken contains DEFAULT_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldBeFound("uploadToken.contains=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the leasesFileUploadList where uploadToken contains UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.contains=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    public void getAllLeasesFileUploadsByUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        // Get all the leasesFileUploadList where uploadToken does not contain DEFAULT_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldNotBeFound("uploadToken.doesNotContain=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the leasesFileUploadList where uploadToken does not contain UPDATED_UPLOAD_TOKEN
        defaultLeasesFileUploadShouldBeFound("uploadToken.doesNotContain=" + UPDATED_UPLOAD_TOKEN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeasesFileUploadShouldBeFound(String filter) throws Exception {
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].leasesFileTypeId").value(hasItem(DEFAULT_LEASES_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));

        // Check, that the count call also returns 1
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeasesFileUploadShouldNotBeFound(String filter) throws Exception {
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeasesFileUpload() throws Exception {
        // Get the leasesFileUpload
        restLeasesFileUploadMockMvc.perform(get("/api/leases-file-uploads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeasesFileUpload() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        int databaseSizeBeforeUpdate = leasesFileUploadRepository.findAll().size();

        // Update the leasesFileUpload
        LeasesFileUpload updatedLeasesFileUpload = leasesFileUploadRepository.findById(leasesFileUpload.getId()).get();
        // Disconnect from session so that the updates on updatedLeasesFileUpload are not directly saved in db
        em.detach(updatedLeasesFileUpload);
        updatedLeasesFileUpload
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .leasesFileTypeId(UPDATED_LEASES_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(updatedLeasesFileUpload);

        restLeasesFileUploadMockMvc.perform(put("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isOk());

        // Validate the LeasesFileUpload in the database
        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeUpdate);
        LeasesFileUpload testLeasesFileUpload = leasesFileUploadList.get(leasesFileUploadList.size() - 1);
        assertThat(testLeasesFileUpload.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeasesFileUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testLeasesFileUpload.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testLeasesFileUpload.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testLeasesFileUpload.getLeasesFileTypeId()).isEqualTo(UPDATED_LEASES_FILE_TYPE_ID);
        assertThat(testLeasesFileUpload.getDataFile()).isEqualTo(UPDATED_DATA_FILE);
        assertThat(testLeasesFileUpload.getDataFileContentType()).isEqualTo(UPDATED_DATA_FILE_CONTENT_TYPE);
        assertThat(testLeasesFileUpload.isUploadSuccessful()).isEqualTo(UPDATED_UPLOAD_SUCCESSFUL);
        assertThat(testLeasesFileUpload.isUploadProcessed()).isEqualTo(UPDATED_UPLOAD_PROCESSED);
        assertThat(testLeasesFileUpload.getUploadToken()).isEqualTo(UPDATED_UPLOAD_TOKEN);

        // Validate the LeasesFileUpload in Elasticsearch
        verify(mockLeasesFileUploadSearchRepository, times(1)).save(testLeasesFileUpload);
    }

    @Test
    @Transactional
    public void updateNonExistingLeasesFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = leasesFileUploadRepository.findAll().size();

        // Create the LeasesFileUpload
        LeasesFileUploadDTO leasesFileUploadDTO = leasesFileUploadMapper.toDto(leasesFileUpload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasesFileUploadMockMvc.perform(put("/api/leases-file-uploads")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesFileUpload in the database
        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasesFileUpload in Elasticsearch
        verify(mockLeasesFileUploadSearchRepository, times(0)).save(leasesFileUpload);
    }

    @Test
    @Transactional
    public void deleteLeasesFileUpload() throws Exception {
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);

        int databaseSizeBeforeDelete = leasesFileUploadRepository.findAll().size();

        // Delete the leasesFileUpload
        restLeasesFileUploadMockMvc.perform(delete("/api/leases-file-uploads/{id}", leasesFileUpload.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeasesFileUpload> leasesFileUploadList = leasesFileUploadRepository.findAll();
        assertThat(leasesFileUploadList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeasesFileUpload in Elasticsearch
        verify(mockLeasesFileUploadSearchRepository, times(1)).deleteById(leasesFileUpload.getId());
    }

    @Test
    @Transactional
    public void searchLeasesFileUpload() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leasesFileUploadRepository.saveAndFlush(leasesFileUpload);
        when(mockLeasesFileUploadSearchRepository.search(queryStringQuery("id:" + leasesFileUpload.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leasesFileUpload), PageRequest.of(0, 1), 1));

        // Search the leasesFileUpload
        restLeasesFileUploadMockMvc.perform(get("/api/_search/leases-file-uploads?query=id:" + leasesFileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].leasesFileTypeId").value(hasItem(DEFAULT_LEASES_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }
}
