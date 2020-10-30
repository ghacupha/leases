package io.github.leases.web.rest;

import io.github.leases.domain.LeasesFileType;
import io.github.leases.service.LeasesFileTypeService;
import io.github.leases.web.rest.errors.BadRequestAlertException;
import io.github.leases.service.dto.LeasesFileTypeCriteria;
import io.github.leases.service.LeasesFileTypeQueryService;

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
 * REST controller for managing {@link io.github.leases.domain.LeasesFileType}.
 */
@RestController
@RequestMapping("/api")
public class LeasesFileTypeResource {

    private final Logger log = LoggerFactory.getLogger(LeasesFileTypeResource.class);

    private static final String ENTITY_NAME = "leasesLeasesFileType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeasesFileTypeService leasesFileTypeService;

    private final LeasesFileTypeQueryService leasesFileTypeQueryService;

    public LeasesFileTypeResource(LeasesFileTypeService leasesFileTypeService, LeasesFileTypeQueryService leasesFileTypeQueryService) {
        this.leasesFileTypeService = leasesFileTypeService;
        this.leasesFileTypeQueryService = leasesFileTypeQueryService;
    }

    /**
     * {@code POST  /leases-file-types} : Create a new leasesFileType.
     *
     * @param leasesFileType the leasesFileType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leasesFileType, or with status {@code 400 (Bad Request)} if the leasesFileType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leases-file-types")
    public ResponseEntity<LeasesFileType> createLeasesFileType(@Valid @RequestBody LeasesFileType leasesFileType) throws URISyntaxException {
        log.debug("REST request to save LeasesFileType : {}", leasesFileType);
        if (leasesFileType.getId() != null) {
            throw new BadRequestAlertException("A new leasesFileType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeasesFileType result = leasesFileTypeService.save(leasesFileType);
        return ResponseEntity.created(new URI("/api/leases-file-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leases-file-types} : Updates an existing leasesFileType.
     *
     * @param leasesFileType the leasesFileType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasesFileType,
     * or with status {@code 400 (Bad Request)} if the leasesFileType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leasesFileType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leases-file-types")
    public ResponseEntity<LeasesFileType> updateLeasesFileType(@Valid @RequestBody LeasesFileType leasesFileType) throws URISyntaxException {
        log.debug("REST request to update LeasesFileType : {}", leasesFileType);
        if (leasesFileType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeasesFileType result = leasesFileTypeService.save(leasesFileType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leasesFileType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leases-file-types} : get all the leasesFileTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leasesFileTypes in body.
     */
    @GetMapping("/leases-file-types")
    public ResponseEntity<List<LeasesFileType>> getAllLeasesFileTypes(LeasesFileTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeasesFileTypes by criteria: {}", criteria);
        Page<LeasesFileType> page = leasesFileTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leases-file-types/count} : count all the leasesFileTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leases-file-types/count")
    public ResponseEntity<Long> countLeasesFileTypes(LeasesFileTypeCriteria criteria) {
        log.debug("REST request to count LeasesFileTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(leasesFileTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leases-file-types/:id} : get the "id" leasesFileType.
     *
     * @param id the id of the leasesFileType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leasesFileType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leases-file-types/{id}")
    public ResponseEntity<LeasesFileType> getLeasesFileType(@PathVariable Long id) {
        log.debug("REST request to get LeasesFileType : {}", id);
        Optional<LeasesFileType> leasesFileType = leasesFileTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leasesFileType);
    }

    /**
     * {@code DELETE  /leases-file-types/:id} : delete the "id" leasesFileType.
     *
     * @param id the id of the leasesFileType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leases-file-types/{id}")
    public ResponseEntity<Void> deleteLeasesFileType(@PathVariable Long id) {
        log.debug("REST request to delete LeasesFileType : {}", id);
        leasesFileTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/leases-file-types?query=:query} : search for the leasesFileType corresponding
     * to the query.
     *
     * @param query the query of the leasesFileType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/leases-file-types")
    public ResponseEntity<List<LeasesFileType>> searchLeasesFileTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeasesFileTypes for query {}", query);
        Page<LeasesFileType> page = leasesFileTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
