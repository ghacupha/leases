package io.github.leases.web.rest;

import io.github.leases.service.LeasesMessageTokenService;
import io.github.leases.web.rest.errors.BadRequestAlertException;
import io.github.leases.service.dto.LeasesMessageTokenDTO;
import io.github.leases.service.dto.LeasesMessageTokenCriteria;
import io.github.leases.service.LeasesMessageTokenQueryService;

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
 * REST controller for managing {@link io.github.leases.domain.LeasesMessageToken}.
 */
@RestController
@RequestMapping("/api")
public class LeasesMessageTokenResource {

    private final Logger log = LoggerFactory.getLogger(LeasesMessageTokenResource.class);

    private static final String ENTITY_NAME = "leasesLeasesMessageToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeasesMessageTokenService leasesMessageTokenService;

    private final LeasesMessageTokenQueryService leasesMessageTokenQueryService;

    public LeasesMessageTokenResource(LeasesMessageTokenService leasesMessageTokenService, LeasesMessageTokenQueryService leasesMessageTokenQueryService) {
        this.leasesMessageTokenService = leasesMessageTokenService;
        this.leasesMessageTokenQueryService = leasesMessageTokenQueryService;
    }

    /**
     * {@code POST  /leases-message-tokens} : Create a new leasesMessageToken.
     *
     * @param leasesMessageTokenDTO the leasesMessageTokenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leasesMessageTokenDTO, or with status {@code 400 (Bad Request)} if the leasesMessageToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leases-message-tokens")
    public ResponseEntity<LeasesMessageTokenDTO> createLeasesMessageToken(@Valid @RequestBody LeasesMessageTokenDTO leasesMessageTokenDTO) throws URISyntaxException {
        log.debug("REST request to save LeasesMessageToken : {}", leasesMessageTokenDTO);
        if (leasesMessageTokenDTO.getId() != null) {
            throw new BadRequestAlertException("A new leasesMessageToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeasesMessageTokenDTO result = leasesMessageTokenService.save(leasesMessageTokenDTO);
        return ResponseEntity.created(new URI("/api/leases-message-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leases-message-tokens} : Updates an existing leasesMessageToken.
     *
     * @param leasesMessageTokenDTO the leasesMessageTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasesMessageTokenDTO,
     * or with status {@code 400 (Bad Request)} if the leasesMessageTokenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leasesMessageTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leases-message-tokens")
    public ResponseEntity<LeasesMessageTokenDTO> updateLeasesMessageToken(@Valid @RequestBody LeasesMessageTokenDTO leasesMessageTokenDTO) throws URISyntaxException {
        log.debug("REST request to update LeasesMessageToken : {}", leasesMessageTokenDTO);
        if (leasesMessageTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeasesMessageTokenDTO result = leasesMessageTokenService.save(leasesMessageTokenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leasesMessageTokenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leases-message-tokens} : get all the leasesMessageTokens.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leasesMessageTokens in body.
     */
    @GetMapping("/leases-message-tokens")
    public ResponseEntity<List<LeasesMessageTokenDTO>> getAllLeasesMessageTokens(LeasesMessageTokenCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeasesMessageTokens by criteria: {}", criteria);
        Page<LeasesMessageTokenDTO> page = leasesMessageTokenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leases-message-tokens/count} : count all the leasesMessageTokens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leases-message-tokens/count")
    public ResponseEntity<Long> countLeasesMessageTokens(LeasesMessageTokenCriteria criteria) {
        log.debug("REST request to count LeasesMessageTokens by criteria: {}", criteria);
        return ResponseEntity.ok().body(leasesMessageTokenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leases-message-tokens/:id} : get the "id" leasesMessageToken.
     *
     * @param id the id of the leasesMessageTokenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leasesMessageTokenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leases-message-tokens/{id}")
    public ResponseEntity<LeasesMessageTokenDTO> getLeasesMessageToken(@PathVariable Long id) {
        log.debug("REST request to get LeasesMessageToken : {}", id);
        Optional<LeasesMessageTokenDTO> leasesMessageTokenDTO = leasesMessageTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leasesMessageTokenDTO);
    }

    /**
     * {@code DELETE  /leases-message-tokens/:id} : delete the "id" leasesMessageToken.
     *
     * @param id the id of the leasesMessageTokenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leases-message-tokens/{id}")
    public ResponseEntity<Void> deleteLeasesMessageToken(@PathVariable Long id) {
        log.debug("REST request to delete LeasesMessageToken : {}", id);
        leasesMessageTokenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/leases-message-tokens?query=:query} : search for the leasesMessageToken corresponding
     * to the query.
     *
     * @param query the query of the leasesMessageToken search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/leases-message-tokens")
    public ResponseEntity<List<LeasesMessageTokenDTO>> searchLeasesMessageTokens(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeasesMessageTokens for query {}", query);
        Page<LeasesMessageTokenDTO> page = leasesMessageTokenService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
