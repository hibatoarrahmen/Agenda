package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A ActiviteDept.
 */
@Entity
@Table(name = "activite_dept")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActiviteDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_act")
    private Integer numAct;

    @Column(name = "type_d")
    private String typeD;

    @Column(name = "descript")
    private String descript;

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

    @JsonIgnoreProperties(value = { "activiteDept" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProcesVerbal procesVerbal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "activiteDept")
    @JsonIgnoreProperties(value = { "activiteDept", "departement" }, allowSetters = true)
    private Set<AgendaDept> agendaDepts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "activiteDepts")
    @JsonIgnoreProperties(value = { "agenda", "departements", "activiteDepts" }, allowSetters = true)
    private Set<Employe> employes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ActiviteDept id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumAct() {
        return this.numAct;
    }

    public ActiviteDept numAct(Integer numAct) {
        this.setNumAct(numAct);
        return this;
    }

    public void setNumAct(Integer numAct) {
        this.numAct = numAct;
    }

    public String getTypeD() {
        return this.typeD;
    }

    public ActiviteDept typeD(String typeD) {
        this.setTypeD(typeD);
        return this;
    }

    public void setTypeD(String typeD) {
        this.typeD = typeD;
    }

    public String getDescript() {
        return this.descript;
    }

    public ActiviteDept descript(String descript) {
        this.setDescript(descript);
        return this;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public LocalDate getDateAct() {
        return this.dateAct;
    }

    public ActiviteDept dateAct(LocalDate dateAct) {
        this.setDateAct(dateAct);
        return this;
    }

    public void setDateAct(LocalDate dateAct) {
        this.dateAct = dateAct;
    }

    public LocalDate gethDebut() {
        return this.hDebut;
    }

    public ActiviteDept hDebut(LocalDate hDebut) {
        this.sethDebut(hDebut);
        return this;
    }

    public void sethDebut(LocalDate hDebut) {
        this.hDebut = hDebut;
    }

    public LocalDate gethFin() {
        return this.hFin;
    }

    public ActiviteDept hFin(LocalDate hFin) {
        this.sethFin(hFin);
        return this;
    }

    public void sethFin(LocalDate hFin) {
        this.hFin = hFin;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public ActiviteDept dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCreateur() {
        return this.createur;
    }

    public ActiviteDept createur(String createur) {
        this.setCreateur(createur);
        return this;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public ProcesVerbal getProcesVerbal() {
        return this.procesVerbal;
    }

    public void setProcesVerbal(ProcesVerbal procesVerbal) {
        this.procesVerbal = procesVerbal;
    }

    public ActiviteDept procesVerbal(ProcesVerbal procesVerbal) {
        this.setProcesVerbal(procesVerbal);
        return this;
    }

    public Set<AgendaDept> getAgendaDepts() {
        return this.agendaDepts;
    }

    public void setAgendaDepts(Set<AgendaDept> agendaDepts) {
        if (this.agendaDepts != null) {
            this.agendaDepts.forEach(i -> i.setActiviteDept(null));
        }
        if (agendaDepts != null) {
            agendaDepts.forEach(i -> i.setActiviteDept(this));
        }
        this.agendaDepts = agendaDepts;
    }

    public ActiviteDept agendaDepts(Set<AgendaDept> agendaDepts) {
        this.setAgendaDepts(agendaDepts);
        return this;
    }

    public ActiviteDept addAgendaDept(AgendaDept agendaDept) {
        this.agendaDepts.add(agendaDept);
        agendaDept.setActiviteDept(this);
        return this;
    }

    public ActiviteDept removeAgendaDept(AgendaDept agendaDept) {
        this.agendaDepts.remove(agendaDept);
        agendaDept.setActiviteDept(null);
        return this;
    }

    public Set<Employe> getEmployes() {
        return this.employes;
    }

    public void setEmployes(Set<Employe> employes) {
        if (this.employes != null) {
            this.employes.forEach(i -> i.removeActiviteDept(this));
        }
        if (employes != null) {
            employes.forEach(i -> i.addActiviteDept(this));
        }
        this.employes = employes;
    }

    public ActiviteDept employes(Set<Employe> employes) {
        this.setEmployes(employes);
        return this;
    }

    public ActiviteDept addEmploye(Employe employe) {
        this.employes.add(employe);
        employe.getActiviteDepts().add(this);
        return this;
    }

    public ActiviteDept removeEmploye(Employe employe) {
        this.employes.remove(employe);
        employe.getActiviteDepts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActiviteDept)) {
            return false;
        }
        return getId() != null && getId().equals(((ActiviteDept) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActiviteDept{" +
            "id=" + getId() +
            ", numAct=" + getNumAct() +
            ", typeD='" + getTypeD() + "'" +
            ", descript='" + getDescript() + "'" +
            ", dateAct='" + getDateAct() + "'" +
            ", hDebut='" + gethDebut() + "'" +
            ", hFin='" + gethFin() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", createur='" + getCreateur() + "'" +
            "}";
    }
}
