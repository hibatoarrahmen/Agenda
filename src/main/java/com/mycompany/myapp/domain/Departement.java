package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num")
    private Integer num;

    @Column(name = "nom")
    private String nom;

    @JsonIgnoreProperties(value = { "activiteDept", "departement" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private AgendaDept agendaDept;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "agenda", "departements", "activiteDepts" }, allowSetters = true)
    private Employe employe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNum() {
        return this.num;
    }

    public Departement num(Integer num) {
        this.setNum(num);
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNom() {
        return this.nom;
    }

    public Departement nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public AgendaDept getAgendaDept() {
        return this.agendaDept;
    }

    public void setAgendaDept(AgendaDept agendaDept) {
        this.agendaDept = agendaDept;
    }

    public Departement agendaDept(AgendaDept agendaDept) {
        this.setAgendaDept(agendaDept);
        return this;
    }

    public Employe getEmploye() {
        return this.employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Departement employe(Employe employe) {
        this.setEmploye(employe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return getId() != null && getId().equals(((Departement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", num=" + getNum() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
