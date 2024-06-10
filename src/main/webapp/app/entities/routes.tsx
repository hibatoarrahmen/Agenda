import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Employe from './employe';
import Departement from './departement';
import AgendaDept from './agenda-dept';
import ActiviteDept from './activite-dept';
import ProcesVerbal from './proces-verbal';
import Agenda from './agenda';
import Activite from './activite';
import Alerte from './alerte';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="employe/*" element={<Employe />} />
        <Route path="departement/*" element={<Departement />} />
        <Route path="agenda-dept/*" element={<AgendaDept />} />
        <Route path="activite-dept/*" element={<ActiviteDept />} />
        <Route path="proces-verbal/*" element={<ProcesVerbal />} />
        <Route path="agenda/*" element={<Agenda />} />
        <Route path="activite/*" element={<Activite />} />
        <Route path="alerte/*" element={<Alerte />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
