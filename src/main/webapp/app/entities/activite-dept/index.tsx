import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ActiviteDept from './activite-dept';
import ActiviteDeptDetail from './activite-dept-detail';
import ActiviteDeptUpdate from './activite-dept-update';
import ActiviteDeptDeleteDialog from './activite-dept-delete-dialog';

const ActiviteDeptRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ActiviteDept />} />
    <Route path="new" element={<ActiviteDeptUpdate />} />
    <Route path=":id">
      <Route index element={<ActiviteDeptDetail />} />
      <Route path="edit" element={<ActiviteDeptUpdate />} />
      <Route path="delete" element={<ActiviteDeptDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActiviteDeptRoutes;
