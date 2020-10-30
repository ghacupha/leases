package io.github.leases.web.rest;

import io.github.leases.service.LeasesFileUploadService;
import io.github.leases.web.rest.errors.BadRequestAlertException;
import io.github.leases.service.dto.LeasesFileUploadDTO;
import io.github.leases.service.dto.LeasesFileUploadCriteria;
import io.github.leases.service.LeasesFileUploadQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.leases.domain.LeasesFileUpload}.
 */
@RestController
@RequestMapping("/api")
public class LeasesFileUploadResource {

    private final Logger log = LoggerFactory.getLogger(LeasesFileUploadResource.class);

    private static final String ENTITY_NAME = "leasesLeasesFileUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeasesFileUploadService leasesFileUploadService;

    private final LeasesFileUploadQueryService leasesFileUploadQueryService;

    public LeasesFileUploadResource(LeasesFileUploadService leasesFileUploadService, LeasesFileUploadQueryService leasesFileUploadQueryService) {
        this.leasesFileUploadService = leasesFileUploadService;
        this.leasesFileUploadQueryService = leasesFileUploadQueryService;
    }

    /**
     * {@code POST  /leases-file-uploads} : Create a new leasesFileUpload.
     *
     * @param leasesFileUploadDTO the leasesFileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leasesFileUploadDTO, or with status {@code 400 (Bad Request)} if the leasesFileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leases-file-uploads")
    public ResponseEntity<LeasesFileUploadDTO> createLeasesFileUpload(@Valid @RequestBody LeasesFileUploadDTO leasesFileUploadDTO) throws URISyntaxException {
        log.debug("REST request to save LeasesFileUpload : {}", leasesFileUploadDTO);
        if (leasesFileUploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new leasesFileUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeasesFileUploadDTO result = leasesFileUploadService.save(leasesFileUploadDTO);
        return ResponseEntity.created(new URI("/api/leases-file-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leases-file-uploads} : Updates an existing leasesFileUpload.
     *
     * @param leasesFileUploadDTO the leasesFileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasesFileUploadDTO,
     * or with status {@code 400 (Bad Request)} if the leasesFileUploadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leasesFileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leases-file-uploads")
    public ResponseEntity<LeasesFileUploadDTO> updateLeasesFileUpload(@Valid @RequestBody LeasesFileUploadDTO leasesFileUploadDTO) throws URISyntaxException {
        log.debug("REST request to update LeasesFileUpload : {}", leasesFileUploadDTO);
        if (leasesFileUploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeasesFileUploadDTO result = leasesFileUploadService.save(leasesFileUploadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leasesFileUploadDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leases-file-uploads} : get all the leasesFileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leasesFileUploads in body.
     */
    @GetMapping("/leases-file-uploads")
    public ResponseEntity<List<LeasesFileUploadDTO>> getAllLeasesFileUploads(LeasesFileUploadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeasesFileUploads by criteria: {}", criteria);
        Page<LeasesFileUploadDTO> page = leasesFileUploadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leases-file-uploads/count} : count all the leasesFileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leases-file-uploads/count")
    public ResponseEntity<Long> countLeasesFileUploads(LeasesFileUploadCriteria criteria) {
        log.debug("REST request to count LeasesFileUploads by criteria: {}", criteria);
        return ResponseEntity.ok().body(leasesFileUploadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leases-file-uploads/:id} : get the "id" leasesFileUpload.
     *
     * @param id the id of the leasesFileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leasesFileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leases-file-uploads/{id}")
    public ResponseEntity<LeasesFileUploadDTO> getLeasesFileUpload(@PathVariable Long id) {
        log.debug("REST request to get LeasesFileUpload : {}", id);
        Optional<LeasesFileUploadDTO> leasesFileUploadDTO = leasesFileUploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leasesFileUploadDTO);
    }

    /**
     * {@code DELETE  /leases-file-uploads/:id} : delete the "id" leasesFileUpload.
     *
     * @param id the id of the leasesFileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leases-file-uploads/{id}")
    public ResponseEntity<Void> deleteLeasesFileUpload(@PathVariable Long id) {
        log.debug("REST request to delete LeasesFileUpload : {}", id);
        leasesFileUploadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/leases-file-uploads?query=:query} : search for the leasesFileUpload corresponding
     * to the query.
     *
     * @param query the query of the leasesFileUpload search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/leases-file-uploads")
    public ResponseEntity<List<LeasesFileUploadDTO>> searchLeasesFileUploads(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeasesFileUploads for query {}", query);
        Page<LeasesFileUploadDTO> page = leasesFileUploadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
