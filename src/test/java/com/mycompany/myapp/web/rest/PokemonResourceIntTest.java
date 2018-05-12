package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.PokebaseApp;

import com.mycompany.myapp.domain.Pokemon;
import com.mycompany.myapp.repository.PokemonRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.TypePo;
import com.mycompany.myapp.domain.enumeration.TypePo;
/**
 * Test class for the PokemonResource REST controller.
 *
 * @see PokemonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokebaseApp.class)
public class PokemonResourceIntTest {

    private static final String DEFAULT_POKENAME = "AAAAAAAAAA";
    private static final String UPDATED_POKENAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final TypePo DEFAULT_TYPE = TypePo.ACIER;
    private static final TypePo UPDATED_TYPE = TypePo.COMBAT;

    private static final TypePo DEFAULT_TYPE_2 = TypePo.ACIER;
    private static final TypePo UPDATED_TYPE_2 = TypePo.COMBAT;

    private static final Integer DEFAULT_LOWEVO = 1;
    private static final Integer UPDATED_LOWEVO = 2;

    private static final Integer DEFAULT_UPPEVO = 1;
    private static final Integer UPDATED_UPPEVO = 2;

    private static final String DEFAULT_IMG = "AAAAAAAAAA";
    private static final String UPDATED_IMG = "BBBBBBBBBB";

    @Autowired
    private PokemonRepository pokemonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPokemonMockMvc;

    private Pokemon pokemon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PokemonResource pokemonResource = new PokemonResource(pokemonRepository);
        this.restPokemonMockMvc = MockMvcBuilders.standaloneSetup(pokemonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pokemon createEntity(EntityManager em) {
        Pokemon pokemon = new Pokemon()
            .pokename(DEFAULT_POKENAME)
            .numero(DEFAULT_NUMERO)
            .type(DEFAULT_TYPE)
            .type2(DEFAULT_TYPE_2)
            .lowevo(DEFAULT_LOWEVO)
            .uppevo(DEFAULT_UPPEVO)
            .img(DEFAULT_IMG);
        return pokemon;
    }

    @Before
    public void initTest() {
        pokemon = createEntity(em);
    }

    @Test
    @Transactional
    public void createPokemon() throws Exception {
        int databaseSizeBeforeCreate = pokemonRepository.findAll().size();

        // Create the Pokemon
        restPokemonMockMvc.perform(post("/api/pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isCreated());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeCreate + 1);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getPokename()).isEqualTo(DEFAULT_POKENAME);
        assertThat(testPokemon.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPokemon.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPokemon.getType2()).isEqualTo(DEFAULT_TYPE_2);
        assertThat(testPokemon.getLowevo()).isEqualTo(DEFAULT_LOWEVO);
        assertThat(testPokemon.getUppevo()).isEqualTo(DEFAULT_UPPEVO);
        assertThat(testPokemon.getImg()).isEqualTo(DEFAULT_IMG);
    }

    @Test
    @Transactional
    public void createPokemonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pokemonRepository.findAll().size();

        // Create the Pokemon with an existing ID
        pokemon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPokemonMockMvc.perform(post("/api/pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isBadRequest());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPokemons() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get all the pokemonList
        restPokemonMockMvc.perform(get("/api/pokemons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pokemon.getId().intValue())))
            .andExpect(jsonPath("$.[*].pokename").value(hasItem(DEFAULT_POKENAME.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type2").value(hasItem(DEFAULT_TYPE_2.toString())))
            .andExpect(jsonPath("$.[*].lowevo").value(hasItem(DEFAULT_LOWEVO)))
            .andExpect(jsonPath("$.[*].uppevo").value(hasItem(DEFAULT_UPPEVO)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG.toString())));
    }

    @Test
    @Transactional
    public void getPokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);

        // Get the pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", pokemon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pokemon.getId().intValue()))
            .andExpect(jsonPath("$.pokename").value(DEFAULT_POKENAME.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.type2").value(DEFAULT_TYPE_2.toString()))
            .andExpect(jsonPath("$.lowevo").value(DEFAULT_LOWEVO))
            .andExpect(jsonPath("$.uppevo").value(DEFAULT_UPPEVO))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPokemon() throws Exception {
        // Get the pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Update the pokemon
        Pokemon updatedPokemon = pokemonRepository.findOne(pokemon.getId());
        // Disconnect from session so that the updates on updatedPokemon are not directly saved in db
        em.detach(updatedPokemon);
        updatedPokemon
            .pokename(UPDATED_POKENAME)
            .numero(UPDATED_NUMERO)
            .type(UPDATED_TYPE)
            .type2(UPDATED_TYPE_2)
            .lowevo(UPDATED_LOWEVO)
            .uppevo(UPDATED_UPPEVO)
            .img(UPDATED_IMG);

        restPokemonMockMvc.perform(put("/api/pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPokemon)))
            .andExpect(status().isOk());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate);
        Pokemon testPokemon = pokemonList.get(pokemonList.size() - 1);
        assertThat(testPokemon.getPokename()).isEqualTo(UPDATED_POKENAME);
        assertThat(testPokemon.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPokemon.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPokemon.getType2()).isEqualTo(UPDATED_TYPE_2);
        assertThat(testPokemon.getLowevo()).isEqualTo(UPDATED_LOWEVO);
        assertThat(testPokemon.getUppevo()).isEqualTo(UPDATED_UPPEVO);
        assertThat(testPokemon.getImg()).isEqualTo(UPDATED_IMG);
    }

    @Test
    @Transactional
    public void updateNonExistingPokemon() throws Exception {
        int databaseSizeBeforeUpdate = pokemonRepository.findAll().size();

        // Create the Pokemon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPokemonMockMvc.perform(put("/api/pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokemon)))
            .andExpect(status().isCreated());

        // Validate the Pokemon in the database
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePokemon() throws Exception {
        // Initialize the database
        pokemonRepository.saveAndFlush(pokemon);
        int databaseSizeBeforeDelete = pokemonRepository.findAll().size();

        // Get the pokemon
        restPokemonMockMvc.perform(delete("/api/pokemons/{id}", pokemon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pokemon> pokemonList = pokemonRepository.findAll();
        assertThat(pokemonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pokemon.class);
        Pokemon pokemon1 = new Pokemon();
        pokemon1.setId(1L);
        Pokemon pokemon2 = new Pokemon();
        pokemon2.setId(pokemon1.getId());
        assertThat(pokemon1).isEqualTo(pokemon2);
        pokemon2.setId(2L);
        assertThat(pokemon1).isNotEqualTo(pokemon2);
        pokemon1.setId(null);
        assertThat(pokemon1).isNotEqualTo(pokemon2);
    }
}
