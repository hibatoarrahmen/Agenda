import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlerte } from 'app/shared/model/alerte.model';
import { getEntity, updateEntity, createEntity, reset } from './alerte.reducer';

export const AlerteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alerteEntity = useAppSelector(state => state.alerte.entity);
  const loading = useAppSelector(state => state.alerte.loading);
  const updating = useAppSelector(state => state.alerte.updating);
  const updateSuccess = useAppSelector(state => state.alerte.updateSuccess);

  const handleClose = () => {
    navigate('/alerte');
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
    if (values.delais !== undefined && typeof values.delais !== 'number') {
      values.delais = Number(values.delais);
    }

    const entity = {
      ...alerteEntity,
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
          ...alerteEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.alerte.home.createOrEditLabel" data-cy="AlerteCreateUpdateHeading">
            Créer ou éditer un Alerte
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="alerte-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Type" id="alerte-type" name="type" data-cy="type" type="text" />
              <ValidatedField label="Delais" id="alerte-delais" name="delais" data-cy="delais" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/alerte" replace color="info">
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

export default AlerteUpdate;
