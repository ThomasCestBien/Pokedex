package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.PokebaseApp;

import com.mycompany.myapp.domain.Objet;
import com.mycompany.myapp.repository.ObjetRepository;
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

import com.mycompany.myapp.domain.enumeration.TypeObj;
/**
 * Test class for the ObjetResource REST controller.
 *
 * @see ObjetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokebaseApp.class)
public class ObjetResourceIntTest {

    private static final String DEFAULT_OBJNAME = "AAAAAAAAAA";
    private static final String UPDATED_OBJNAME = "BBBBBBBBBB";

    private static final TypeObj DEFAULT_TYPE = TypeObj.POTION;
    private static final TypeObj UPDATED_TYPE = TypeObj.BALL;

    private static final String DEFAULT_IMG = "AAAAAAAAAA";
    private static final String UPDATED_IMG = "BBBBBBBBBB";

    @Autowired
    private ObjetRepository objetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restObjetMockMvc;

    private Objet objet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ObjetResource objetResource = new ObjetResource(objetRepository);
        this.restObjetMockMvc = MockMvcBuilders.standaloneSetup(objetResource)
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
    public static Objet createEntity(EntityManager em) {
        Objet objet = new Objet()
            .objname(DEFAULT_OBJNAME)
            .type(DEFAULT_TYPE)
            .img(DEFAULT_IMG);
        return objet;
    }

    @Before
    public void initTest() {
        objet = createEntity(em);
    }

    @Test
    @Transactional
    public void createObjet() throws Exception {
        int databaseSizeBeforeCreate = objetRepository.findAll().size();

        // Create the Objet
        restObjetMockMvc.perform(post("/api/objets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objet)))
            .andExpect(status().isCreated());

        // Validate the Objet in the database
        List<Objet> objetList = objetRepository.findAll();
        assertThat(objetList).hasSize(databaseSizeBeforeCreate + 1);
        Objet testObjet = objetList.get(objetList.size() - 1);
        assertThat(testObjet.getObjname()).isEqualTo(DEFAULT_OBJNAME);
        assertThat(testObjet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testObjet.getImg()).isEqualTo(DEFAULT_IMG);
    }

    @Test
    @Transactional
    public void createObjetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = objetRepository.findAll().size();

        // Create the Objet with an existing ID
        objet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restObjetMockMvc.perform(post("/api/objets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objet)))
            .andExpect(status().isBadRequest());

        // Validate the Objet in the database
        List<Objet> objetList = objetRepository.findAll();
        assertThat(objetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllObjets() throws Exception {
        // Initialize the database
        objetRepository.saveAndFlush(objet);

        // Get all the objetList
        restObjetMockMvc.perform(get("/api/objets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(objet.getId().intValue())))
            .andExpect(jsonPath("$.[*].objname").value(hasItem(DEFAULT_OBJNAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG.toString())));
    }

    @Test
    @Transactional
    public void getObjet() throws Exception {
        // Initialize the database
        objetRepository.saveAndFlush(objet);

        // Get the objet
        restObjetMockMvc.perform(get("/api/objets/{id}", objet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(objet.getId().intValue()))
            .andExpect(jsonPath("$.objname").value(DEFAULT_OBJNAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingObjet() throws Exception {
        // Get the objet
        restObjetMockMvc.perform(get("/api/objets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObjet() throws Exception {
        // Initialize the database
        objetRepository.saveAndFlush(objet);
        int databaseSizeBeforeUpdate = objetRepository.findAll().size();

        // Update the objet
        Objet updatedObjet = objetRepository.findOne(objet.getId());
        // Disconnect from session so that the updates on updatedObjet are not directly saved in db
        em.detach(updatedObjet);
        updatedObjet
            .objname(UPDATED_OBJNAME)
            .type(UPDATED_TYPE)
            .img(UPDATED_IMG);

        restObjetMockMvc.perform(put("/api/objets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedObjet)))
            .andExpect(status().isOk());

        // Validate the Objet in the database
        List<Objet> objetList = objetRepository.findAll();
        assertThat(objetList).hasSize(databaseSizeBeforeUpdate);
        Objet testObjet = objetList.get(objetList.size() - 1);
        assertThat(testObjet.getObjname()).isEqualTo(UPDATED_OBJNAME);
        assertThat(testObjet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testObjet.getImg()).isEqualTo(UPDATED_IMG);
    }

    @Test
    @Transactional
    public void updateNonExistingObjet() throws Exception {
        int databaseSizeBeforeUpdate = objetRepository.findAll().size();

        // Create the Objet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restObjetMockMvc.perform(put("/api/objets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(objet)))
            .andExpect(status().isCreated());

        // Validate the Objet in the database
        List<Objet> objetList = objetRepository.findAll();
        assertThat(objetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteObjet() throws Exception {
        // Initialize the database
        objetRepository.saveAndFlush(objet);
        int databaseSizeBeforeDelete = objetRepository.findAll().size();

        // Get the objet
        restObjetMockMvc.perform(delete("/api/objets/{id}", objet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Objet> objetList = objetRepository.findAll();
        assertThat(objetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objet.class);
        Objet objet1 = new Objet();
        objet1.setId(1L);
        Objet objet2 = new Objet();
        objet2.setId(objet1.getId());
        assertThat(objet1).isEqualTo(objet2);
        objet2.setId(2L);
        assertThat(objet1).isNotEqualTo(objet2);
        objet1.setId(null);
        assertThat(objet1).isNotEqualTo(objet2);
    }
}
