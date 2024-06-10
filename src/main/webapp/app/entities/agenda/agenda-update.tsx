import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActivite } from 'app/shared/model/activite.model';
import { getEntities as getActivites } from 'app/entities/activite/activite.reducer';
import { IAgenda } from 'app/shared/model/agenda.model';
import { getEntity, updateEntity, createEntity, reset } from './agenda.reducer';

export const AgendaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activites = useAppSelector(state => state.activite.entities);
  const agendaEntity = useAppSelector(state => state.agenda.entity);
  const loading = useAppSelector(state => state.agenda.loading);
  const updating = useAppSelector(state => state.agenda.updating);
  const updateSuccess = useAppSelector(state => state.agenda.updateSuccess);

  const handleClose = () => {
    navigate('/agenda');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActivites({}));
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
    if (values.numAgenda !== undefined && typeof values.numAgenda !== 'number') {
      values.numAgenda = Number(values.numAgenda);
    }

    const entity = {
      ...agendaEntity,
      ...values,
      activite: activites.find(it => it.id.toString() === values.activite?.toString()),
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
          ...agendaEntity,
          activite: agendaEntity?.activite?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.agenda.home.createOrEditLabel" data-cy="AgendaCreateUpdateHeading">
            Créer ou éditer un Agenda
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="agenda-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Num Agenda" id="agenda-numAgenda" name="numAgenda" data-cy="numAgenda" type="text" />
              <ValidatedField label="Date Creation" id="agenda-dateCreation" name="dateCreation" data-cy="dateCreation" type="date" />
              <ValidatedField id="agenda-activite" name="activite" data-cy="activite" label="Activite" type="select">
                <option value="" key="0" />
                {activites
                  ? activites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agenda" replace color="info">
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

export default AgendaUpdate;
