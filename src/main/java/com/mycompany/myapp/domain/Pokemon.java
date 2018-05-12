package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.TypePo;

/**
 * A Pokemon.
 */
@Entity
@Table(name = "pokemon")
public class Pokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokename")
    private String pokename;

    @Column(name = "numero")
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private TypePo type;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_2")
    private TypePo type2;

    @Column(name = "lowevo")
    private Integer lowevo;

    @Column(name = "uppevo")
    private Integer uppevo;

    @Column(name = "img")
    private String img;

    @ManyToOne
    private Pokedex pokedex;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPokename() {
        return pokename;
    }

    public Pokemon pokename(String pokename) {
        this.pokename = pokename;
        return this;
    }

    public void setPokename(String pokename) {
        this.pokename = pokename;
    }

    public Integer getNumero() {
        return numero;
    }

    public Pokemon numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public TypePo getType() {
        return type;
    }

    public Pokemon type(TypePo type) {
        this.type = type;
        return this;
    }

    public void setType(TypePo type) {
        this.type = type;
    }

    public TypePo getType2() {
        return type2;
    }

    public Pokemon type2(TypePo type2) {
        this.type2 = type2;
        return this;
    }

    public void setType2(TypePo type2) {
        this.type2 = type2;
    }

    public Integer getLowevo() {
        return lowevo;
    }

    public Pokemon lowevo(Integer lowevo) {
        this.lowevo = lowevo;
        return this;
    }

    public void setLowevo(Integer lowevo) {
        this.lowevo = lowevo;
    }

    public Integer getUppevo() {
        return uppevo;
    }

    public Pokemon uppevo(Integer uppevo) {
        this.uppevo = uppevo;
        return this;
    }

    public void setUppevo(Integer uppevo) {
        this.uppevo = uppevo;
    }

    public String getImg() {
        return img;
    }

    public Pokemon img(String img) {
        this.img = img;
        return this;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Pokedex getPokedex() {
        return pokedex;
    }

    public Pokemon pokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
        return this;
    }

    public void setPokedex(Pokedex pokedex) {
        this.pokedex = pokedex;
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
        Pokemon pokemon = (Pokemon) o;
        if (pokemon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pokemon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Pokemon{" +
            "id=" + getId() +
            ", pokename='" + getPokename() + "'" +
            ", numero=" + getNumero() +
            ", type='" + getType() + "'" +
            ", type2='" + getType2() + "'" +
            ", lowevo=" + getLowevo() +
            ", uppevo=" + getUppevo() +
            ", img='" + getImg() + "'" +
            "}";
    }
}
