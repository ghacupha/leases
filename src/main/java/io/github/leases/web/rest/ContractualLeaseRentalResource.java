package io.github.leases.web.rest;

import io.github.leases.service.ContractualLeaseRentalService;
import io.github.leases.web.rest.errors.BadRequestAlertException;
import io.github.leases.service.dto.ContractualLeaseRentalDTO;
import io.github.leases.service.dto.ContractualLeaseRentalCriteria;
import io.github.leases.service.ContractualLeaseRentalQueryService;

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
 * REST controller for managing {@link io.github.leases.domain.ContractualLeaseRental}.
 */
@RestController
@RequestMapping("/api")
public class ContractualLeaseRentalResource {

    private final Logger log = LoggerFactory.getLogger(ContractualLeaseRentalResource.class);

    private static final String ENTITY_NAME = "contractualLeaseRental";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractualLeaseRentalService contractualLeaseRentalService;

    private final ContractualLeaseRentalQueryService contractualLeaseRentalQueryService;

    public ContractualLeaseRentalResource(ContractualLeaseRentalService contractualLeaseRentalService, ContractualLeaseRentalQueryService contractualLeaseRentalQueryService) {
        this.contractualLeaseRentalService = contractualLeaseRentalService;
        this.contractualLeaseRentalQueryService = contractualLeaseRentalQueryService;
    }

    /**
     * {@code POST  /contractual-lease-rentals} : Create a new contractualLeaseRental.
     *
     * @param contractualLeaseRentalDTO the contractualLeaseRentalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractualLeaseRentalDTO, or with status {@code 400 (Bad Request)} if the contractualLeaseRental has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contractual-lease-rentals")
    public ResponseEntity<ContractualLeaseRentalDTO> createContractualLeaseRental(@Valid @RequestBody ContractualLeaseRentalDTO contractualLeaseRentalDTO) throws URISyntaxException {
        log.debug("REST request to save ContractualLeaseRental : {}", contractualLeaseRentalDTO);
        if (contractualLeaseRentalDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractualLeaseRental cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractualLeaseRentalDTO result = contractualLeaseRentalService.save(contractualLeaseRentalDTO);
        return ResponseEntity.created(new URI("/api/contractual-lease-rentals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contractual-lease-rentals} : Updates an existing contractualLeaseRental.
     *
     * @param contractualLeaseRentalDTO the contractualLeaseRentalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractualLeaseRentalDTO,
     * or with status {@code 400 (Bad Request)} if the contractualLeaseRentalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractualLeaseRentalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contractual-lease-rentals")
    public ResponseEntity<ContractualLeaseRentalDTO> updateContractualLeaseRental(@Valid @RequestBody ContractualLeaseRentalDTO contractualLeaseRentalDTO) throws URISyntaxException {
        log.debug("REST request to update ContractualLeaseRental : {}", contractualLeaseRentalDTO);
        if (contractualLeaseRentalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContractualLeaseRentalDTO result = contractualLeaseRentalService.save(contractualLeaseRentalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractualLeaseRentalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contractual-lease-rentals} : get all the contractualLeaseRentals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractualLeaseRentals in body.
     */
    @GetMapping("/contractual-lease-rentals")
    public ResponseEntity<List<ContractualLeaseRentalDTO>> getAllContractualLeaseRentals(ContractualLeaseRentalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContractualLeaseRentals by criteria: {}", criteria);
        Page<ContractualLeaseRentalDTO> page = contractualLeaseRentalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contractual-lease-rentals/count} : count all the contractualLeaseRentals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contractual-lease-rentals/count")
    public ResponseEntity<Long> countContractualLeaseRentals(ContractualLeaseRentalCriteria criteria) {
        log.debug("REST request to count ContractualLeaseRentals by criteria: {}", criteria);
        return ResponseEntity.ok().body(contractualLeaseRentalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contractual-lease-rentals/:id} : get the "id" contractualLeaseRental.
     *
     * @param id the id of the contractualLeaseRentalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractualLeaseRentalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contractual-lease-rentals/{id}")
    public ResponseEntity<ContractualLeaseRentalDTO> getContractualLeaseRental(@PathVariable Long id) {
        log.debug("REST request to get ContractualLeaseRental : {}", id);
        Optional<ContractualLeaseRentalDTO> contractualLeaseRentalDTO = contractualLeaseRentalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractualLeaseRentalDTO);
    }

    /**
     * {@code DELETE  /contractual-lease-rentals/:id} : delete the "id" contractualLeaseRental.
     *
     * @param id the id of the contractualLeaseRentalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contractual-lease-rentals/{id}")
    public ResponseEntity<Void> deleteContractualLeaseRental(@PathVariable Long id) {
        log.debug("REST request to delete ContractualLeaseRental : {}", id);
        contractualLeaseRentalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/contractual-lease-rentals?query=:query} : search for the contractualLeaseRental corresponding
     * to the query.
     *
     * @param query the query of the contractualLeaseRental search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contractual-lease-rentals")
    public ResponseEntity<List<ContractualLeaseRentalDTO>> searchContractualLeaseRentals(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContractualLeaseRentals for query {}", query);
        Page<ContractualLeaseRentalDTO> page = contractualLeaseRentalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
