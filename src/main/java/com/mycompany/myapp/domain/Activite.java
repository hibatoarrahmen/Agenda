package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Activite.
 */
@Entity
@Table(name = "activite")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_activite")
    private Integer numActivite;

    @Column(name = "type_a")
    private String typeA;

    @Column(name = "description")
    private String description;

    @Column(name = "date_act")
    private LocalDate dateAct;

    @Column(name = "h_debut")
    private LocalDate hDebut;

    @Column(name = "h_fin")
    private LocalDate hFin;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "createur")
    private String createur;

    @Column(name = "visible")
    private Integer visible;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activite")
    @JsonIgnoreProperties(value = { "activite", "employe" }, allowSetters = true)
    private Set<Agenda> agenda = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "activites" }, allowSetters = true)
    private Alerte alerte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumActivite() {
        return this.numActivite;
    }

    public Activite numActivite(Integer numActivite) {
        this.setNumActivite(numActivite);
        return this;
    }

    public void setNumActivite(Integer numActivite) {
        this.numActivite = numActivite;
    }

    public String getTypeA() {
        return this.typeA;
    }

    public Activite typeA(String typeA) {
        this.setTypeA(typeA);
        return this;
    }

    public void setTypeA(String typeA) {
        this.typeA = typeA;
    }

    public String getDescription() {
        return this.description;
    }

    public Activite description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateAct() {
        return this.dateAct;
    }

    public Activite dateAct(LocalDate dateAct) {
        this.setDateAct(dateAct);
        return this;
    }

    public void setDateAct(LocalDate dateAct) {
        this.dateAct = dateAct;
    }

    public LocalDate gethDebut() {
        return this.hDebut;
    }

    public Activite hDebut(LocalDate hDebut) {
        this.sethDebut(hDebut);
        return this;
    }

    public void sethDebut(LocalDate hDebut) {
        this.hDebut = hDebut;
    }

    public LocalDate gethFin() {
        return this.hFin;
    }

    public Activite hFin(LocalDate hFin) {
        this.sethFin(hFin);
        return this;
    }

    public void sethFin(LocalDate hFin) {
        this.hFin = hFin;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Activite dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCreateur() {
        return this.createur;
    }

    public Activite createur(String createur) {
        this.setCreateur(createur);
        return this;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public Integer getVisible() {
        return this.visible;
    }

    public Activite visible(Integer visible) {
        this.setVisible(visible);
        return this;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Set<Agenda> getAgenda() {
        return this.agenda;
    }

    public void setAgenda(Set<Agenda> agenda) {
        if (this.agenda != null) {
            this.agenda.forEach(i -> i.setActivite(null));
        }
        if (agenda != null) {
            agenda.forEach(i -> i.setActivite(this));
        }
        this.agenda = agenda;
    }

    public Activite agenda(Set<Agenda> agenda) {
        this.setAgenda(agenda);
        return this;
    }

    public Activite addAgenda(Agenda agenda) {
        this.agenda.add(agenda);
        agenda.setActivite(this);
        return this;
    }

    public Activite removeAgenda(Agenda agenda) {
        this.agenda.remove(agenda);
        agenda.setActivite(null);
        return this;
    }

    public Alerte getAlerte() {
        return this.alerte;
    }

    public void setAlerte(Alerte alerte) {
        this.alerte = alerte;
    }

    public Activite alerte(Alerte alerte) {
        this.setAlerte(alerte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activite)) {
            return false;
        }
        return getId() != null && getId().equals(((Activite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activite{" +
            "id=" + getId() +
            ", numActivite=" + getNumActivite() +
            ", typeA='" + getTypeA() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateAct='" + getDateAct() + "'" +
            ", hDebut='" + gethDebut() + "'" +
            ", hFin='" + gethFin() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", createur='" + getCreateur() + "'" +
            ", visible=" + getVisible() +
            "}";
    }
}
