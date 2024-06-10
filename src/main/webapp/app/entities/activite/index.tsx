import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Activite from './activite';
import ActiviteDetail from './activite-detail';
import ActiviteUpdate from './activite-update';
import ActiviteDeleteDialog from './activite-delete-dialog';

const ActiviteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Activite />} />
    <Route path="new" element={<ActiviteUpdate />} />
    <Route path=":id">
      <Route index element={<ActiviteDetail />} />
      <Route path="edit" element={<ActiviteUpdate />} />
      <Route path="delete" element={<ActiviteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActiviteRoutes;
