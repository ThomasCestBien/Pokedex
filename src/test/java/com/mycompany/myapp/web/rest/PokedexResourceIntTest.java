package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.PokebaseApp;

import com.mycompany.myapp.domain.Pokedex;
import com.mycompany.myapp.repository.PokedexRepository;
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

/**
 * Test class for the PokedexResource REST controller.
 *
 * @see PokedexResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokebaseApp.class)
public class PokedexResourceIntTest {

    private static final String DEFAULT_POKEDEXNAME = "AAAAAAAAAA";
    private static final String UPDATED_POKEDEXNAME = "BBBBBBBBBB";

    @Autowired
    private PokedexRepository pokedexRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPokedexMockMvc;

    private Pokedex pokedex;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PokedexResource pokedexResource = new PokedexResource(pokedexRepository);
        this.restPokedexMockMvc = MockMvcBuilders.standaloneSetup(pokedexResource)
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
    public static Pokedex createEntity(EntityManager em) {
        Pokedex pokedex = new Pokedex()
            .pokedexname(DEFAULT_POKEDEXNAME);
        return pokedex;
    }

    @Before
    public void initTest() {
        pokedex = createEntity(em);
    }

    @Test
    @Transactional
    public void createPokedex() throws Exception {
        int databaseSizeBeforeCreate = pokedexRepository.findAll().size();

        // Create the Pokedex
        restPokedexMockMvc.perform(post("/api/pokedexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isCreated());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeCreate + 1);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
        assertThat(testPokedex.getPokedexname()).isEqualTo(DEFAULT_POKEDEXNAME);
    }

    @Test
    @Transactional
    public void createPokedexWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pokedexRepository.findAll().size();

        // Create the Pokedex with an existing ID
        pokedex.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPokedexMockMvc.perform(post("/api/pokedexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isBadRequest());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPokedexes() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        // Get all the pokedexList
        restPokedexMockMvc.perform(get("/api/pokedexes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pokedex.getId().intValue())))
            .andExpect(jsonPath("$.[*].pokedexname").value(hasItem(DEFAULT_POKEDEXNAME.toString())));
    }

    @Test
    @Transactional
    public void getPokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);

        // Get the pokedex
        restPokedexMockMvc.perform(get("/api/pokedexes/{id}", pokedex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pokedex.getId().intValue()))
            .andExpect(jsonPath("$.pokedexname").value(DEFAULT_POKEDEXNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPokedex() throws Exception {
        // Get the pokedex
        restPokedexMockMvc.perform(get("/api/pokedexes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();

        // Update the pokedex
        Pokedex updatedPokedex = pokedexRepository.findOne(pokedex.getId());
        // Disconnect from session so that the updates on updatedPokedex are not directly saved in db
        em.detach(updatedPokedex);
        updatedPokedex
            .pokedexname(UPDATED_POKEDEXNAME);

        restPokedexMockMvc.perform(put("/api/pokedexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPokedex)))
            .andExpect(status().isOk());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate);
        Pokedex testPokedex = pokedexList.get(pokedexList.size() - 1);
        assertThat(testPokedex.getPokedexname()).isEqualTo(UPDATED_POKEDEXNAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPokedex() throws Exception {
        int databaseSizeBeforeUpdate = pokedexRepository.findAll().size();

        // Create the Pokedex

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPokedexMockMvc.perform(put("/api/pokedexes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pokedex)))
            .andExpect(status().isCreated());

        // Validate the Pokedex in the database
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePokedex() throws Exception {
        // Initialize the database
        pokedexRepository.saveAndFlush(pokedex);
        int databaseSizeBeforeDelete = pokedexRepository.findAll().size();

        // Get the pokedex
        restPokedexMockMvc.perform(delete("/api/pokedexes/{id}", pokedex.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pokedex> pokedexList = pokedexRepository.findAll();
        assertThat(pokedexList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pokedex.class);
        Pokedex pokedex1 = new Pokedex();
        pokedex1.setId(1L);
        Pokedex pokedex2 = new Pokedex();
        pokedex2.setId(pokedex1.getId());
        assertThat(pokedex1).isEqualTo(pokedex2);
        pokedex2.setId(2L);
        assertThat(pokedex1).isNotEqualTo(pokedex2);
        pokedex1.setId(null);
        assertThat(pokedex1).isNotEqualTo(pokedex2);
    }
}
