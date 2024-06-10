package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ProcesVerbal.
 */
@Entity
@Table(name = "proces_verbal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcesVerbal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_proces_v")
    private Integer numProcesV;

    @Column(name = "resum")
    private String resum;

    @JsonIgnoreProperties(value = { "procesVerbal", "agendaDepts", "employes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "procesVerbal")
    private ActiviteDept activiteDept;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProcesVerbal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumProcesV() {
        return this.numProcesV;
    }

    public ProcesVerbal numProcesV(Integer numProcesV) {
        this.setNumProcesV(numProcesV);
        return this;
    }

    public void setNumProcesV(Integer numProcesV) {
        this.numProcesV = numProcesV;
    }

    public String getResum() {
        return this.resum;
    }

    public ProcesVerbal resum(String resum) {
        this.setResum(resum);
        return this;
    }

    public void setResum(String resum) {
        this.resum = resum;
    }

    public ActiviteDept getActiviteDept() {
        return this.activiteDept;
    }

    public void setActiviteDept(ActiviteDept activiteDept) {
        if (this.activiteDept != null) {
            this.activiteDept.setProcesVerbal(null);
        }
        if (activiteDept != null) {
            activiteDept.setProcesVerbal(this);
        }
        this.activiteDept = activiteDept;
    }

    public ProcesVerbal activiteDept(ActiviteDept activiteDept) {
        this.setActiviteDept(activiteDept);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcesVerbal)) {
            return false;
        }
        return getId() != null && getId().equals(((ProcesVerbal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcesVerbal{" +
            "id=" + getId() +
            ", numProcesV=" + getNumProcesV() +
            ", resum='" + getResum() + "'" +
            "}";
    }
}
