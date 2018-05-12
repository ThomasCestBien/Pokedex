package com.mycompany.myapp.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.TypeObj;

/**
 * A Objet.
 */
@Entity
@Table(name = "objet")
public class Objet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "objname")
    private String objname;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private TypeObj type;

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

    public String getObjname() {
        return objname;
    }

    public Objet objname(String objname) {
        this.objname = objname;
        return this;
    }

    public void setObjname(String objname) {
        this.objname = objname;
    }

    public TypeObj getType() {
        return type;
    }

    public Objet type(TypeObj type) {
        this.type = type;
        return this;
    }

    public void setType(TypeObj type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public Objet img(String img) {
        this.img = img;
        return this;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Pokedex getPokedex() {
        return pokedex;
    }

    public Objet pokedex(Pokedex pokedex) {
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
        Objet objet = (Objet) o;
        if (objet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), objet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Objet{" +
            "id=" + getId() +
            ", objname='" + getObjname() + "'" +
            ", type='" + getType() + "'" +
            ", img='" + getImg() + "'" +
            "}";
    }
}
