package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pokedex;

import com.mycompany.myapp.repository.PokedexRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pokedex.
 */
@RestController
@RequestMapping("/api")
public class PokedexResource {

    private final Logger log = LoggerFactory.getLogger(PokedexResource.class);

    private static final String ENTITY_NAME = "pokedex";

    private final PokedexRepository pokedexRepository;

    public PokedexResource(PokedexRepository pokedexRepository) {
        this.pokedexRepository = pokedexRepository;
    }

    /**
     * POST  /pokedexes : Create a new pokedex.
     *
     * @param pokedex the pokedex to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pokedex, or with status 400 (Bad Request) if the pokedex has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pokedexes")
    @Timed
    public ResponseEntity<Pokedex> createPokedex(@RequestBody Pokedex pokedex) throws URISyntaxException {
        log.debug("REST request to save Pokedex : {}", pokedex);
        if (pokedex.getId() != null) {
            throw new BadRequestAlertException("A new pokedex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pokedex result = pokedexRepository.save(pokedex);
        return ResponseEntity.created(new URI("/api/pokedexes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pokedexes : Updates an existing pokedex.
     *
     * @param pokedex the pokedex to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pokedex,
     * or with status 400 (Bad Request) if the pokedex is not valid,
     * or with status 500 (Internal Server Error) if the pokedex couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pokedexes")
    @Timed
    public ResponseEntity<Pokedex> updatePokedex(@RequestBody Pokedex pokedex) throws URISyntaxException {
        log.debug("REST request to update Pokedex : {}", pokedex);
        if (pokedex.getId() == null) {
            return createPokedex(pokedex);
        }
        Pokedex result = pokedexRepository.save(pokedex);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pokedex.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pokedexes : get all the pokedexes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pokedexes in body
     */
    @GetMapping("/pokedexes")
    @Timed
    public List<Pokedex> getAllPokedexes() {
        log.debug("REST request to get all Pokedexes");
        return pokedexRepository.findAll();
        }

    /**
     * GET  /pokedexes/:id : get the "id" pokedex.
     *
     * @param id the id of the pokedex to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pokedex, or with status 404 (Not Found)
     */
    @GetMapping("/pokedexes/{id}")
    @Timed
    public ResponseEntity<Pokedex> getPokedex(@PathVariable Long id) {
        log.debug("REST request to get Pokedex : {}", id);
        Pokedex pokedex = pokedexRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pokedex));
    }

    /**
     * DELETE  /pokedexes/:id : delete the "id" pokedex.
     *
     * @param id the id of the pokedex to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pokedexes/{id}")
    @Timed
    public ResponseEntity<Void> deletePokedex(@PathVariable Long id) {
        log.debug("REST request to delete Pokedex : {}", id);
        pokedexRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
