import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActiviteDept } from 'app/shared/model/activite-dept.model';
import { getEntities as getActiviteDepts } from 'app/entities/activite-dept/activite-dept.reducer';
import { IAgendaDept } from 'app/shared/model/agenda-dept.model';
import { getEntity, updateEntity, createEntity, reset } from './agenda-dept.reducer';

export const AgendaDeptUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const activiteDepts = useAppSelector(state => state.activiteDept.entities);
  const agendaDeptEntity = useAppSelector(state => state.agendaDept.entity);
  const loading = useAppSelector(state => state.agendaDept.loading);
  const updating = useAppSelector(state => state.agendaDept.updating);
  const updateSuccess = useAppSelector(state => state.agendaDept.updateSuccess);

  const handleClose = () => {
    navigate('/agenda-dept');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActiviteDepts({}));
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
      ...agendaDeptEntity,
      ...values,
      activiteDept: activiteDepts.find(it => it.id.toString() === values.activiteDept?.toString()),
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
          ...agendaDeptEntity,
          activiteDept: agendaDeptEntity?.activiteDept?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.agendaDept.home.createOrEditLabel" data-cy="AgendaDeptCreateUpdateHeading">
            Créer ou éditer un Agenda Dept
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="agenda-dept-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Num Agenda" id="agenda-dept-numAgenda" name="numAgenda" data-cy="numAgenda" type="text" />
              <ValidatedField label="Date MAJ" id="agenda-dept-dateMAJ" name="dateMAJ" data-cy="dateMAJ" type="date" />
              <ValidatedField id="agenda-dept-activiteDept" name="activiteDept" data-cy="activiteDept" label="Activite Dept" type="select">
                <option value="" key="0" />
                {activiteDepts
                  ? activiteDepts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agenda-dept" replace color="info">
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

export default AgendaDeptUpdate;
