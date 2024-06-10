import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgendaDept from './agenda-dept';
import AgendaDeptDetail from './agenda-dept-detail';
import AgendaDeptUpdate from './agenda-dept-update';
import AgendaDeptDeleteDialog from './agenda-dept-delete-dialog';

const AgendaDeptRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgendaDept />} />
    <Route path="new" element={<AgendaDeptUpdate />} />
    <Route path=":id">
      <Route index element={<AgendaDeptDetail />} />
      <Route path="edit" element={<AgendaDeptUpdate />} />
      <Route path="delete" element={<AgendaDeptDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgendaDeptRoutes;
