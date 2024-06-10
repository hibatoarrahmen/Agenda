import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Employe from './employe';
import EmployeDetail from './employe-detail';
import EmployeUpdate from './employe-update';
import EmployeDeleteDialog from './employe-delete-dialog';

const EmployeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Employe />} />
    <Route path="new" element={<EmployeUpdate />} />
    <Route path=":id">
      <Route index element={<EmployeDetail />} />
      <Route path="edit" element={<EmployeUpdate />} />
      <Route path="delete" element={<EmployeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EmployeRoutes;
