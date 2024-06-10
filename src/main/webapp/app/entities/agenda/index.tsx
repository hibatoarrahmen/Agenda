import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Agenda from './agenda';
import AgendaDetail from './agenda-detail';
import AgendaUpdate from './agenda-update';
import AgendaDeleteDialog from './agenda-delete-dialog';

const AgendaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Agenda />} />
    <Route path="new" element={<AgendaUpdate />} />
    <Route path=":id">
      <Route index element={<AgendaDetail />} />
      <Route path="edit" element={<AgendaUpdate />} />
      <Route path="delete" element={<AgendaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgendaRoutes;
