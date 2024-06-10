package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Alerte.
 */
@Entity
@Table(name = "alerte")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Alerte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "delais")
    private Integer delais;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "alerte")
    @JsonIgnoreProperties(value = { "agenda", "alerte" }, allowSetters = true)
    private Set<Activite> activites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Alerte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Alerte type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDelais() {
        return this.delais;
    }

    public Alerte delais(Integer delais) {
        this.setDelais(delais);
        return this;
    }

    public void setDelais(Integer delais) {
        this.delais = delais;
    }

    public Set<Activite> getActivites() {
        return this.activites;
    }

    public void setActivites(Set<Activite> activites) {
        if (this.activites != null) {
            this.activites.forEach(i -> i.setAlerte(null));
        }
        if (activites != null) {
            activites.forEach(i -> i.setAlerte(this));
        }
        this.activites = activites;
    }

    public Alerte activites(Set<Activite> activites) {
        this.setActivites(activites);
        return this;
    }

    public Alerte addActivite(Activite activite) {
        this.activites.add(activite);
        activite.setAlerte(this);
        return this;
    }

    public Alerte removeActivite(Activite activite) {
        this.activites.remove(activite);
        activite.setAlerte(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alerte)) {
            return false;
        }
        return getId() != null && getId().equals(((Alerte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Alerte{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", delais=" + getDelais() +
            "}";
    }
}
