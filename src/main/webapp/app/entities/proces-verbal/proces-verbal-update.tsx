import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProcesVerbal } from 'app/shared/model/proces-verbal.model';
import { getEntity, updateEntity, createEntity, reset } from './proces-verbal.reducer';

export const ProcesVerbalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const procesVerbalEntity = useAppSelector(state => state.procesVerbal.entity);
  const loading = useAppSelector(state => state.procesVerbal.loading);
  const updating = useAppSelector(state => state.procesVerbal.updating);
  const updateSuccess = useAppSelector(state => state.procesVerbal.updateSuccess);

  const handleClose = () => {
    navigate('/proces-verbal');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.numProcesV !== undefined && typeof values.numProcesV !== 'number') {
      values.numProcesV = Number(values.numProcesV);
    }

    const entity = {
      ...procesVerbalEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...procesVerbalEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.procesVerbal.home.createOrEditLabel" data-cy="ProcesVerbalCreateUpdateHeading">
            Créer ou éditer un Proces Verbal
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="proces-verbal-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Num Proces V" id="proces-verbal-numProcesV" name="numProcesV" data-cy="numProcesV" type="text" />
              <ValidatedField label="Resum" id="proces-verbal-resum" name="resum" data-cy="resum" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/proces-verbal" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Retour</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Sauvegarder
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProcesVerbalUpdate;
