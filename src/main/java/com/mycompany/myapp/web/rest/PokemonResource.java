package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Pokemon;

import com.mycompany.myapp.repository.PokemonRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pokemon.
 */
@RestController
@RequestMapping("/api")
public class PokemonResource {

    private final Logger log = LoggerFactory.getLogger(PokemonResource.class);

    private static final String ENTITY_NAME = "pokemon";

    private final PokemonRepository pokemonRepository;

    public PokemonResource(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    /**
     * POST  /pokemons : Create a new pokemon.
     *
     * @param pokemon the pokemon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pokemon, or with status 400 (Bad Request) if the pokemon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pokemons")
    @Timed
    public ResponseEntity<Pokemon> createPokemon(@RequestBody Pokemon pokemon) throws URISyntaxException {
        log.debug("REST request to save Pokemon : {}", pokemon);
        if (pokemon.getId() != null) {
            throw new BadRequestAlertException("A new pokemon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pokemon result = pokemonRepository.save(pokemon);
        return ResponseEntity.created(new URI("/api/pokemons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pokemons : Updates an existing pokemon.
     *
     * @param pokemon the pokemon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pokemon,
     * or with status 400 (Bad Request) if the pokemon is not valid,
     * or with status 500 (Internal Server Error) if the pokemon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pokemons")
    @Timed
    public ResponseEntity<Pokemon> updatePokemon(@RequestBody Pokemon pokemon) throws URISyntaxException {
        log.debug("REST request to update Pokemon : {}", pokemon);
        if (pokemon.getId() == null) {
            return createPokemon(pokemon);
        }
        Pokemon result = pokemonRepository.save(pokemon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pokemon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pokemons : get all the pokemons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pokemons in body
     */
    @GetMapping("/pokemons")
    @Timed
    public ResponseEntity<List<Pokemon>> getAllPokemons(Pageable pageable) {
        log.debug("REST request to get a page of Pokemons");
        Page<Pokemon> page = pokemonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pokemons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pokemons/:id : get the "id" pokemon.
     *
     * @param id the id of the pokemon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pokemon, or with status 404 (Not Found)
     */
    @GetMapping("/pokemons/{id}")
    @Timed
    public ResponseEntity<Pokemon> getPokemon(@PathVariable Long id) {
        log.debug("REST request to get Pokemon : {}", id);
        Pokemon pokemon = pokemonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pokemon));
    }

    /**
     * DELETE  /pokemons/:id : delete the "id" pokemon.
     *
     * @param id the id of the pokemon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pokemons/{id}")
    @Timed
    public ResponseEntity<Void> deletePokemon(@PathVariable Long id) {
        log.debug("REST request to delete Pokemon : {}", id);
        pokemonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
