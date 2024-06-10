import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ProcesVerbal from './proces-verbal';
import ProcesVerbalDetail from './proces-verbal-detail';
import ProcesVerbalUpdate from './proces-verbal-update';
import ProcesVerbalDeleteDialog from './proces-verbal-delete-dialog';

const ProcesVerbalRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ProcesVerbal />} />
    <Route path="new" element={<ProcesVerbalUpdate />} />
    <Route path=":id">
      <Route index element={<ProcesVerbalDetail />} />
      <Route path="edit" element={<ProcesVerbalUpdate />} />
      <Route path="delete" element={<ProcesVerbalDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProcesVerbalRoutes;
