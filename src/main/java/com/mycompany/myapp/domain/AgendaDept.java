package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AgendaDept.
 */
@Entity
@Table(name = "agenda_dept")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgendaDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_agenda")
    private Integer numAgenda;

    @Column(name = "date_maj")
    private LocalDate dateMAJ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "procesVerbal", "agendaDepts", "employes" }, allowSetters = true)
    private ActiviteDept activiteDept;

    @JsonIgnoreProperties(value = { "agendaDept", "employe" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "agendaDept")
    private Departement departement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AgendaDept id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumAgenda() {
        return this.numAgenda;
    }

    public AgendaDept numAgenda(Integer numAgenda) {
        this.setNumAgenda(numAgenda);
        return this;
    }

    public void setNumAgenda(Integer numAgenda) {
        this.numAgenda = numAgenda;
    }

    public LocalDate getDateMAJ() {
        return this.dateMAJ;
    }

    public AgendaDept dateMAJ(LocalDate dateMAJ) {
        this.setDateMAJ(dateMAJ);
        return this;
    }

    public void setDateMAJ(LocalDate dateMAJ) {
        this.dateMAJ = dateMAJ;
    }

    public ActiviteDept getActiviteDept() {
        return this.activiteDept;
    }

    public void setActiviteDept(ActiviteDept activiteDept) {
        this.activiteDept = activiteDept;
    }

    public AgendaDept activiteDept(ActiviteDept activiteDept) {
        this.setActiviteDept(activiteDept);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        if (this.departement != null) {
            this.departement.setAgendaDept(null);
        }
        if (departement != null) {
            departement.setAgendaDept(this);
        }
        this.departement = departement;
    }

    public AgendaDept departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgendaDept)) {
            return false;
        }
        return getId() != null && getId().equals(((AgendaDept) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgendaDept{" +
            "id=" + getId() +
            ", numAgenda=" + getNumAgenda() +
            ", dateMAJ='" + getDateMAJ() + "'" +
            "}";
    }
}
