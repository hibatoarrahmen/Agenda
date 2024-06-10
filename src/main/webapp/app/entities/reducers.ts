import employe from 'app/entities/employe/employe.reducer';
import departement from 'app/entities/departement/departement.reducer';
import agendaDept from 'app/entities/agenda-dept/agenda-dept.reducer';
import activiteDept from 'app/entities/activite-dept/activite-dept.reducer';
import procesVerbal from 'app/entities/proces-verbal/proces-verbal.reducer';
import agenda from 'app/entities/agenda/agenda.reducer';
import activite from 'app/entities/activite/activite.reducer';
import alerte from 'app/entities/alerte/alerte.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  employe,
  departement,
  agendaDept,
  activiteDept,
  procesVerbal,
  agenda,
  activite,
  alerte,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
