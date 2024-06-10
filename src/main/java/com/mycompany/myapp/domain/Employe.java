package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employe.
 */
@Entity
@Table(name = "employe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_employe")
    private Integer numEmploye;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "tel_intern")
    private String telIntern;

    @Column(name = "email")
    private String email;

    @Column(name = "niveau")
    private Integer niveau;

    @JsonIgnoreProperties(value = { "activite", "employe" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Agenda agenda;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employe")
    @JsonIgnoreProperties(value = { "agendaDept", "employe" }, allowSetters = true)
    private Set<Departement> departements = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_employe__activite_dept",
        joinColumns = @JoinColumn(name = "employe_id"),
        inverseJoinColumns = @JoinColumn(name = "activite_dept_id")
    )
    @JsonIgnoreProperties(value = { "procesVerbal", "agendaDepts", "employes" }, allowSetters = true)
    private Set<ActiviteDept> activiteDepts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumEmploye() {
        return this.numEmploye;
    }

    public Employe numEmploye(Integer numEmploye) {
        this.setNumEmploye(numEmploye);
        return this;
    }

    public void setNumEmploye(Integer numEmploye) {
        this.numEmploye = numEmploye;
    }

    public String getNom() {
        return this.nom;
    }

    public Employe nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Employe prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelIntern() {
        return this.telIntern;
    }

    public Employe telIntern(String telIntern) {
        this.setTelIntern(telIntern);
        return this;
    }

    public void setTelIntern(String telIntern) {
        this.telIntern = telIntern;
    }

    public String getEmail() {
        return this.email;
    }

    public Employe email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNiveau() {
        return this.niveau;
    }

    public Employe niveau(Integer niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }

    public Agenda getAgenda() {
        return this.agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Employe agenda(Agenda agenda) {
        this.setAgenda(agenda);
        return this;
    }

    public Set<Departement> getDepartements() {
        return this.departements;
    }

    public void setDepartements(Set<Departement> departements) {
        if (this.departements != null) {
            this.departements.forEach(i -> i.setEmploye(null));
        }
        if (departements != null) {
            departements.forEach(i -> i.setEmploye(this));
        }
        this.departements = departements;
    }

    public Employe departements(Set<Departement> departements) {
        this.setDepartements(departements);
        return this;
    }

    public Employe addDepartement(Departement departement) {
        this.departements.add(departement);
        departement.setEmploye(this);
        return this;
    }

    public Employe removeDepartement(Departement departement) {
        this.departements.remove(departement);
        departement.setEmploye(null);
        return this;
    }

    public Set<ActiviteDept> getActiviteDepts() {
        return this.activiteDepts;
    }

    public void setActiviteDepts(Set<ActiviteDept> activiteDepts) {
        this.activiteDepts = activiteDepts;
    }

    public Employe activiteDepts(Set<ActiviteDept> activiteDepts) {
        this.setActiviteDepts(activiteDepts);
        return this;
    }

    public Employe addActiviteDept(ActiviteDept activiteDept) {
        this.activiteDepts.add(activiteDept);
        return this;
    }

    public Employe removeActiviteDept(ActiviteDept activiteDept) {
        this.activiteDepts.remove(activiteDept);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employe)) {
            return false;
        }
        return getId() != null && getId().equals(((Employe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employe{" +
            "id=" + getId() +
            ", numEmploye=" + getNumEmploye() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", telIntern='" + getTelIntern() + "'" +
            ", email='" + getEmail() + "'" +
            ", niveau=" + getNiveau() +
            "}";
    }
}
