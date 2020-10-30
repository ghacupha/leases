package io.github.leases.web.rest;

import io.github.leases.LeasesApp;
import io.github.leases.domain.LeasesFileType;
import io.github.leases.repository.LeasesFileTypeRepository;
import io.github.leases.repository.search.LeasesFileTypeSearchRepository;
import io.github.leases.service.LeasesFileTypeService;
import io.github.leases.service.dto.LeasesFileTypeCriteria;
import io.github.leases.service.LeasesFileTypeQueryService;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.leases.domain.enumeration.LeasesFileMediumTypes;
import io.github.leases.domain.enumeration.LeasesFileModelType;
/**
 * Integration tests for the {@link LeasesFileTypeResource} REST controller.
 */
@SpringBootTest(classes = LeasesApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeasesFileTypeResourceIT {

    private static final String DEFAULT_LEASES_FILE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEASES_FILE_TYPE_NAME = "BBBBBBBBBB";

    private static final LeasesFileMediumTypes DEFAULT_LEASES_FILE_MEDIUM_TYPE = LeasesFileMediumTypes.EXCEL;
    private static final LeasesFileMediumTypes UPDATED_LEASES_FILE_MEDIUM_TYPE = LeasesFileMediumTypes.EXCEL_XLS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final LeasesFileModelType DEFAULT_LEASESFILE_TYPE = LeasesFileModelType.CURRENCY_LIST;
    private static final LeasesFileModelType UPDATED_LEASESFILE_TYPE = LeasesFileModelType.CONTRACTUAL_LEASE_RENTAL;

    @Autowired
    private LeasesFileTypeRepository leasesFileTypeRepository;

    @Autowired
    private LeasesFileTypeService leasesFileTypeService;

    /**
     * This repository is mocked in the io.github.leases.repository.search test package.
     *
     * @see io.github.leases.repository.search.LeasesFileTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeasesFileTypeSearchRepository mockLeasesFileTypeSearchRepository;

    @Autowired
    private LeasesFileTypeQueryService leasesFileTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeasesFileTypeMockMvc;

    private LeasesFileType leasesFileType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesFileType createEntity(EntityManager em) {
        LeasesFileType leasesFileType = new LeasesFileType()
            .leasesFileTypeName(DEFAULT_LEASES_FILE_TYPE_NAME)
            .leasesFileMediumType(DEFAULT_LEASES_FILE_MEDIUM_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .fileTemplate(DEFAULT_FILE_TEMPLATE)
            .fileTemplateContentType(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)
            .leasesfileType(DEFAULT_LEASESFILE_TYPE);
        return leasesFileType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasesFileType createUpdatedEntity(EntityManager em) {
        LeasesFileType leasesFileType = new LeasesFileType()
            .leasesFileTypeName(UPDATED_LEASES_FILE_TYPE_NAME)
            .leasesFileMediumType(UPDATED_LEASES_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .leasesfileType(UPDATED_LEASESFILE_TYPE);
        return leasesFileType;
    }

    @BeforeEach
    public void initTest() {
        leasesFileType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeasesFileType() throws Exception {
        int databaseSizeBeforeCreate = leasesFileTypeRepository.findAll().size();
        // Create the LeasesFileType
        restLeasesFileTypeMockMvc.perform(post("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileType)))
            .andExpect(status().isCreated());

        // Validate the LeasesFileType in the database
        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LeasesFileType testLeasesFileType = leasesFileTypeList.get(leasesFileTypeList.size() - 1);
        assertThat(testLeasesFileType.getLeasesFileTypeName()).isEqualTo(DEFAULT_LEASES_FILE_TYPE_NAME);
        assertThat(testLeasesFileType.getLeasesFileMediumType()).isEqualTo(DEFAULT_LEASES_FILE_MEDIUM_TYPE);
        assertThat(testLeasesFileType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeasesFileType.getFileTemplate()).isEqualTo(DEFAULT_FILE_TEMPLATE);
        assertThat(testLeasesFileType.getFileTemplateContentType()).isEqualTo(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testLeasesFileType.getLeasesfileType()).isEqualTo(DEFAULT_LEASESFILE_TYPE);

        // Validate the LeasesFileType in Elasticsearch
        verify(mockLeasesFileTypeSearchRepository, times(1)).save(testLeasesFileType);
    }

    @Test
    @Transactional
    public void createLeasesFileTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leasesFileTypeRepository.findAll().size();

        // Create the LeasesFileType with an existing ID
        leasesFileType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeasesFileTypeMockMvc.perform(post("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileType)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesFileType in the database
        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeasesFileType in Elasticsearch
        verify(mockLeasesFileTypeSearchRepository, times(0)).save(leasesFileType);
    }


    @Test
    @Transactional
    public void checkLeasesFileTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesFileTypeRepository.findAll().size();
        // set the field null
        leasesFileType.setLeasesFileTypeName(null);

        // Create the LeasesFileType, which fails.


        restLeasesFileTypeMockMvc.perform(post("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileType)))
            .andExpect(status().isBadRequest());

        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLeasesFileMediumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = leasesFileTypeRepository.findAll().size();
        // set the field null
        leasesFileType.setLeasesFileMediumType(null);

        // Create the LeasesFileType, which fails.


        restLeasesFileTypeMockMvc.perform(post("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileType)))
            .andExpect(status().isBadRequest());

        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypes() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].leasesFileTypeName").value(hasItem(DEFAULT_LEASES_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].leasesFileMediumType").value(hasItem(DEFAULT_LEASES_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].leasesfileType").value(hasItem(DEFAULT_LEASESFILE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getLeasesFileType() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get the leasesFileType
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types/{id}", leasesFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leasesFileType.getId().intValue()))
            .andExpect(jsonPath("$.leasesFileTypeName").value(DEFAULT_LEASES_FILE_TYPE_NAME))
            .andExpect(jsonPath("$.leasesFileMediumType").value(DEFAULT_LEASES_FILE_MEDIUM_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileTemplateContentType").value(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileTemplate").value(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE)))
            .andExpect(jsonPath("$.leasesfileType").value(DEFAULT_LEASESFILE_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getLeasesFileTypesByIdFiltering() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        Long id = leasesFileType.getId();

        defaultLeasesFileTypeShouldBeFound("id.equals=" + id);
        defaultLeasesFileTypeShouldNotBeFound("id.notEquals=" + id);

        defaultLeasesFileTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeasesFileTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultLeasesFileTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeasesFileTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName equals to DEFAULT_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.equals=" + DEFAULT_LEASES_FILE_TYPE_NAME);

        // Get all the leasesFileTypeList where leasesFileTypeName equals to UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.equals=" + UPDATED_LEASES_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName not equals to DEFAULT_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.notEquals=" + DEFAULT_LEASES_FILE_TYPE_NAME);

        // Get all the leasesFileTypeList where leasesFileTypeName not equals to UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.notEquals=" + UPDATED_LEASES_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName in DEFAULT_LEASES_FILE_TYPE_NAME or UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.in=" + DEFAULT_LEASES_FILE_TYPE_NAME + "," + UPDATED_LEASES_FILE_TYPE_NAME);

        // Get all the leasesFileTypeList where leasesFileTypeName equals to UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.in=" + UPDATED_LEASES_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName is not null
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.specified=true");

        // Get all the leasesFileTypeList where leasesFileTypeName is null
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameContainsSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName contains DEFAULT_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.contains=" + DEFAULT_LEASES_FILE_TYPE_NAME);

        // Get all the leasesFileTypeList where leasesFileTypeName contains UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.contains=" + UPDATED_LEASES_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileTypeName does not contain DEFAULT_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldNotBeFound("leasesFileTypeName.doesNotContain=" + DEFAULT_LEASES_FILE_TYPE_NAME);

        // Get all the leasesFileTypeList where leasesFileTypeName does not contain UPDATED_LEASES_FILE_TYPE_NAME
        defaultLeasesFileTypeShouldBeFound("leasesFileTypeName.doesNotContain=" + UPDATED_LEASES_FILE_TYPE_NAME);
    }


    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileMediumTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileMediumType equals to DEFAULT_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesFileMediumType.equals=" + DEFAULT_LEASES_FILE_MEDIUM_TYPE);

        // Get all the leasesFileTypeList where leasesFileMediumType equals to UPDATED_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesFileMediumType.equals=" + UPDATED_LEASES_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileMediumTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileMediumType not equals to DEFAULT_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesFileMediumType.notEquals=" + DEFAULT_LEASES_FILE_MEDIUM_TYPE);

        // Get all the leasesFileTypeList where leasesFileMediumType not equals to UPDATED_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesFileMediumType.notEquals=" + UPDATED_LEASES_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileMediumTypeIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileMediumType in DEFAULT_LEASES_FILE_MEDIUM_TYPE or UPDATED_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesFileMediumType.in=" + DEFAULT_LEASES_FILE_MEDIUM_TYPE + "," + UPDATED_LEASES_FILE_MEDIUM_TYPE);

        // Get all the leasesFileTypeList where leasesFileMediumType equals to UPDATED_LEASES_FILE_MEDIUM_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesFileMediumType.in=" + UPDATED_LEASES_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesFileMediumTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesFileMediumType is not null
        defaultLeasesFileTypeShouldBeFound("leasesFileMediumType.specified=true");

        // Get all the leasesFileTypeList where leasesFileMediumType is null
        defaultLeasesFileTypeShouldNotBeFound("leasesFileMediumType.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description equals to DEFAULT_DESCRIPTION
        defaultLeasesFileTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileTypeList where description equals to UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultLeasesFileTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileTypeList where description not equals to UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leasesFileTypeList where description equals to UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description is not null
        defaultLeasesFileTypeShouldBeFound("description.specified=true");

        // Get all the leasesFileTypeList where description is null
        defaultLeasesFileTypeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description contains DEFAULT_DESCRIPTION
        defaultLeasesFileTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileTypeList where description contains UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultLeasesFileTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leasesFileTypeList where description does not contain UPDATED_DESCRIPTION
        defaultLeasesFileTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesfileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesfileType equals to DEFAULT_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesfileType.equals=" + DEFAULT_LEASESFILE_TYPE);

        // Get all the leasesFileTypeList where leasesfileType equals to UPDATED_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesfileType.equals=" + UPDATED_LEASESFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesfileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesfileType not equals to DEFAULT_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesfileType.notEquals=" + DEFAULT_LEASESFILE_TYPE);

        // Get all the leasesFileTypeList where leasesfileType not equals to UPDATED_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesfileType.notEquals=" + UPDATED_LEASESFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesfileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesfileType in DEFAULT_LEASESFILE_TYPE or UPDATED_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldBeFound("leasesfileType.in=" + DEFAULT_LEASESFILE_TYPE + "," + UPDATED_LEASESFILE_TYPE);

        // Get all the leasesFileTypeList where leasesfileType equals to UPDATED_LEASESFILE_TYPE
        defaultLeasesFileTypeShouldNotBeFound("leasesfileType.in=" + UPDATED_LEASESFILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllLeasesFileTypesByLeasesfileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasesFileTypeRepository.saveAndFlush(leasesFileType);

        // Get all the leasesFileTypeList where leasesfileType is not null
        defaultLeasesFileTypeShouldBeFound("leasesfileType.specified=true");

        // Get all the leasesFileTypeList where leasesfileType is null
        defaultLeasesFileTypeShouldNotBeFound("leasesfileType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeasesFileTypeShouldBeFound(String filter) throws Exception {
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].leasesFileTypeName").value(hasItem(DEFAULT_LEASES_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].leasesFileMediumType").value(hasItem(DEFAULT_LEASES_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].leasesfileType").value(hasItem(DEFAULT_LEASESFILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeasesFileTypeShouldNotBeFound(String filter) throws Exception {
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeasesFileType() throws Exception {
        // Get the leasesFileType
        restLeasesFileTypeMockMvc.perform(get("/api/leases-file-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeasesFileType() throws Exception {
        // Initialize the database
        leasesFileTypeService.save(leasesFileType);

        int databaseSizeBeforeUpdate = leasesFileTypeRepository.findAll().size();

        // Update the leasesFileType
        LeasesFileType updatedLeasesFileType = leasesFileTypeRepository.findById(leasesFileType.getId()).get();
        // Disconnect from session so that the updates on updatedLeasesFileType are not directly saved in db
        em.detach(updatedLeasesFileType);
        updatedLeasesFileType
            .leasesFileTypeName(UPDATED_LEASES_FILE_TYPE_NAME)
            .leasesFileMediumType(UPDATED_LEASES_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .leasesfileType(UPDATED_LEASESFILE_TYPE);

        restLeasesFileTypeMockMvc.perform(put("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeasesFileType)))
            .andExpect(status().isOk());

        // Validate the LeasesFileType in the database
        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeUpdate);
        LeasesFileType testLeasesFileType = leasesFileTypeList.get(leasesFileTypeList.size() - 1);
        assertThat(testLeasesFileType.getLeasesFileTypeName()).isEqualTo(UPDATED_LEASES_FILE_TYPE_NAME);
        assertThat(testLeasesFileType.getLeasesFileMediumType()).isEqualTo(UPDATED_LEASES_FILE_MEDIUM_TYPE);
        assertThat(testLeasesFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeasesFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testLeasesFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testLeasesFileType.getLeasesfileType()).isEqualTo(UPDATED_LEASESFILE_TYPE);

        // Validate the LeasesFileType in Elasticsearch
        verify(mockLeasesFileTypeSearchRepository, times(2)).save(testLeasesFileType);
    }

    @Test
    @Transactional
    public void updateNonExistingLeasesFileType() throws Exception {
        int databaseSizeBeforeUpdate = leasesFileTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasesFileTypeMockMvc.perform(put("/api/leases-file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(leasesFileType)))
            .andExpect(status().isBadRequest());

        // Validate the LeasesFileType in the database
        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasesFileType in Elasticsearch
        verify(mockLeasesFileTypeSearchRepository, times(0)).save(leasesFileType);
    }

    @Test
    @Transactional
    public void deleteLeasesFileType() throws Exception {
        // Initialize the database
        leasesFileTypeService.save(leasesFileType);

        int databaseSizeBeforeDelete = leasesFileTypeRepository.findAll().size();

        // Delete the leasesFileType
        restLeasesFileTypeMockMvc.perform(delete("/api/leases-file-types/{id}", leasesFileType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeasesFileType> leasesFileTypeList = leasesFileTypeRepository.findAll();
        assertThat(leasesFileTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeasesFileType in Elasticsearch
        verify(mockLeasesFileTypeSearchRepository, times(1)).deleteById(leasesFileType.getId());
    }

    @Test
    @Transactional
    public void searchLeasesFileType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leasesFileTypeService.save(leasesFileType);
        when(mockLeasesFileTypeSearchRepository.search(queryStringQuery("id:" + leasesFileType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leasesFileType), PageRequest.of(0, 1), 1));

        // Search the leasesFileType
        restLeasesFileTypeMockMvc.perform(get("/api/_search/leases-file-types?query=id:" + leasesFileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasesFileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].leasesFileTypeName").value(hasItem(DEFAULT_LEASES_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].leasesFileMediumType").value(hasItem(DEFAULT_LEASES_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].leasesfileType").value(hasItem(DEFAULT_LEASESFILE_TYPE.toString())));
    }
}
