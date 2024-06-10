import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAlerte } from 'app/shared/model/alerte.model';
import { getEntities as getAlertes } from 'app/entities/alerte/alerte.reducer';
import { IActivite } from 'app/shared/model/activite.model';
import { getEntity, updateEntity, createEntity, reset } from './activite.reducer';

export const ActiviteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alertes = useAppSelector(state => state.alerte.entities);
  const activiteEntity = useAppSelector(state => state.activite.entity);
  const loading = useAppSelector(state => state.activite.loading);
  const updating = useAppSelector(state => state.activite.updating);
  const updateSuccess = useAppSelector(state => state.activite.updateSuccess);

  const handleClose = () => {
    navigate('/activite');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAlertes({}));
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
    if (values.numActivite !== undefined && typeof values.numActivite !== 'number') {
      values.numActivite = Number(values.numActivite);
    }
    if (values.visible !== undefined && typeof values.visible !== 'number') {
      values.visible = Number(values.visible);
    }

    const entity = {
      ...activiteEntity,
      ...values,
      alerte: alertes.find(it => it.id.toString() === values.alerte?.toString()),
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
          ...activiteEntity,
          alerte: activiteEntity?.alerte?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.activite.home.createOrEditLabel" data-cy="ActiviteCreateUpdateHeading">
            Créer ou éditer un Activite
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="activite-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Num Activite" id="activite-numActivite" name="numActivite" data-cy="numActivite" type="text" />
              <ValidatedField label="Type A" id="activite-typeA" name="typeA" data-cy="typeA" type="text" />
              <ValidatedField label="Description" id="activite-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Date Act" id="activite-dateAct" name="dateAct" data-cy="dateAct" type="date" />
              <ValidatedField label="H Debut" id="activite-hDebut" name="hDebut" data-cy="hDebut" type="date" />
              <ValidatedField label="H Fin" id="activite-hFin" name="hFin" data-cy="hFin" type="date" />
              <ValidatedField label="Date Creation" id="activite-dateCreation" name="dateCreation" data-cy="dateCreation" type="date" />
              <ValidatedField label="Createur" id="activite-createur" name="createur" data-cy="createur" type="text" />
              <ValidatedField label="Visible" id="activite-visible" name="visible" data-cy="visible" type="text" />
              <ValidatedField id="activite-alerte" name="alerte" data-cy="alerte" label="Alerte" type="select">
                <option value="" key="0" />
                {alertes
                  ? alertes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activite" replace color="info">
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

export default ActiviteUpdate;
