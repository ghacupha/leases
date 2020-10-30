package io.github.leases.web.rest;

import io.github.leases.service.LeaseDetailsService;
import io.github.leases.web.rest.errors.BadRequestAlertException;
import io.github.leases.service.dto.LeaseDetailsDTO;
import io.github.leases.service.dto.LeaseDetailsCriteria;
import io.github.leases.service.LeaseDetailsQueryService;

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
 * REST controller for managing {@link io.github.leases.domain.LeaseDetails}.
 */
@RestController
@RequestMapping("/api")
public class LeaseDetailsResource {

    private final Logger log = LoggerFactory.getLogger(LeaseDetailsResource.class);

    private static final String ENTITY_NAME = "leaseDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseDetailsService leaseDetailsService;

    private final LeaseDetailsQueryService leaseDetailsQueryService;

    public LeaseDetailsResource(LeaseDetailsService leaseDetailsService, LeaseDetailsQueryService leaseDetailsQueryService) {
        this.leaseDetailsService = leaseDetailsService;
        this.leaseDetailsQueryService = leaseDetailsQueryService;
    }

    /**
     * {@code POST  /lease-details} : Create a new leaseDetails.
     *
     * @param leaseDetailsDTO the leaseDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseDetailsDTO, or with status {@code 400 (Bad Request)} if the leaseDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-details")
    public ResponseEntity<LeaseDetailsDTO> createLeaseDetails(@Valid @RequestBody LeaseDetailsDTO leaseDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save LeaseDetails : {}", leaseDetailsDTO);
        if (leaseDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseDetailsDTO result = leaseDetailsService.save(leaseDetailsDTO);
        return ResponseEntity.created(new URI("/api/lease-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-details} : Updates an existing leaseDetails.
     *
     * @param leaseDetailsDTO the leaseDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the leaseDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-details")
    public ResponseEntity<LeaseDetailsDTO> updateLeaseDetails(@Valid @RequestBody LeaseDetailsDTO leaseDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update LeaseDetails : {}", leaseDetailsDTO);
        if (leaseDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaseDetailsDTO result = leaseDetailsService.save(leaseDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaseDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lease-details} : get all the leaseDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseDetails in body.
     */
    @GetMapping("/lease-details")
    public ResponseEntity<List<LeaseDetailsDTO>> getAllLeaseDetails(LeaseDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseDetails by criteria: {}", criteria);
        Page<LeaseDetailsDTO> page = leaseDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-details/count} : count all the leaseDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-details/count")
    public ResponseEntity<Long> countLeaseDetails(LeaseDetailsCriteria criteria) {
        log.debug("REST request to count LeaseDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-details/:id} : get the "id" leaseDetails.
     *
     * @param id the id of the leaseDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-details/{id}")
    public ResponseEntity<LeaseDetailsDTO> getLeaseDetails(@PathVariable Long id) {
        log.debug("REST request to get LeaseDetails : {}", id);
        Optional<LeaseDetailsDTO> leaseDetailsDTO = leaseDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseDetailsDTO);
    }

    /**
     * {@code DELETE  /lease-details/:id} : delete the "id" leaseDetails.
     *
     * @param id the id of the leaseDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-details/{id}")
    public ResponseEntity<Void> deleteLeaseDetails(@PathVariable Long id) {
        log.debug("REST request to delete LeaseDetails : {}", id);
        leaseDetailsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/lease-details?query=:query} : search for the leaseDetails corresponding
     * to the query.
     *
     * @param query the query of the leaseDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-details")
    public ResponseEntity<List<LeaseDetailsDTO>> searchLeaseDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseDetails for query {}", query);
        Page<LeaseDetailsDTO> page = leaseDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
