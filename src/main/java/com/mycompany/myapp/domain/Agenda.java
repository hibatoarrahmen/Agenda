package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Agenda.
 */
@Entity
@Table(name = "agenda")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_agenda")
    private Integer numAgenda;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agenda", "alerte" }, allowSetters = true)
    private Activite activite;

    @JsonIgnoreProperties(value = { "agenda", "departements", "activiteDepts" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "agenda")
    private Employe employe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Agenda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumAgenda() {
        return this.numAgenda;
    }

    public Agenda numAgenda(Integer numAgenda) {
        this.setNumAgenda(numAgenda);
        return this;
    }

    public void setNumAgenda(Integer numAgenda) {
        this.numAgenda = numAgenda;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Agenda dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Activite getActivite() {
        return this.activite;
    }

    public void setActivite(Activite activite) {
        this.activite = activite;
    }

    public Agenda activite(Activite activite) {
        this.setActivite(activite);
        return this;
    }

    public Employe getEmploye() {
        return this.employe;
    }

    public void setEmploye(Employe employe) {
        if (this.employe != null) {
            this.employe.setAgenda(null);
        }
        if (employe != null) {
            employe.setAgenda(this);
        }
        this.employe = employe;
    }

    public Agenda employe(Employe employe) {
        this.setEmploye(employe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agenda)) {
            return false;
        }
        return getId() != null && getId().equals(((Agenda) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agenda{" +
            "id=" + getId() +
            ", numAgenda=" + getNumAgenda() +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
