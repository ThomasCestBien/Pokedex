package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pokedex.
 */
@Entity
@Table(name = "pokedex")
public class Pokedex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokedexname")
    private String pokedexname;

    @OneToMany(mappedBy = "pokedex")
    @JsonIgnore
    private Set<Pokemon> pokemons = new HashSet<>();

    @OneToMany(mappedBy = "pokedex")
    @JsonIgnore
    private Set<Objet> objets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPokedexname() {
        return pokedexname;
    }

    public Pokedex pokedexname(String pokedexname) {
        this.pokedexname = pokedexname;
        return this;
    }

    public void setPokedexname(String pokedexname) {
        this.pokedexname = pokedexname;
    }

    public Set<Pokemon> getPokemons() {
        return pokemons;
    }

    public Pokedex pokemons(Set<Pokemon> pokemons) {
        this.pokemons = pokemons;
        return this;
    }

    public Pokedex addPokemon(Pokemon pokemon) {
        this.pokemons.add(pokemon);
        pokemon.setPokedex(this);
        return this;
    }

    public Pokedex removePokemon(Pokemon pokemon) {
        this.pokemons.remove(pokemon);
        pokemon.setPokedex(null);
        return this;
    }

    public void setPokemons(Set<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public Set<Objet> getObjets() {
        return objets;
    }

    public Pokedex objets(Set<Objet> objets) {
        this.objets = objets;
        return this;
    }

    public Pokedex addObjet(Objet objet) {
        this.objets.add(objet);
        objet.setPokedex(this);
        return this;
    }

    public Pokedex removeObjet(Objet objet) {
        this.objets.remove(objet);
        objet.setPokedex(null);
        return this;
    }

    public void setObjets(Set<Objet> objets) {
        this.objets = objets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pokedex pokedex = (Pokedex) o;
        if (pokedex.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pokedex.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pokedex{" +
            "id=" + getId() +
            ", pokedexname='" + getPokedexname() + "'" +
            "}";
    }
}
