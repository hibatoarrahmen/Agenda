import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IProcesVerbal } from 'app/shared/model/proces-verbal.model';
import { getEntities as getProcesVerbals } from 'app/entities/proces-verbal/proces-verbal.reducer';
import { IEmploye } from 'app/shared/model/employe.model';
import { getEntities as getEmployes } from 'app/entities/employe/employe.reducer';
import { IActiviteDept } from 'app/shared/model/activite-dept.model';
import { getEntity, updateEntity, createEntity, reset } from './activite-dept.reducer';

export const ActiviteDeptUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const procesVerbals = useAppSelector(state => state.procesVerbal.entities);
  const employes = useAppSelector(state => state.employe.entities);
  const activiteDeptEntity = useAppSelector(state => state.activiteDept.entity);
  const loading = useAppSelector(state => state.activiteDept.loading);
  const updating = useAppSelector(state => state.activiteDept.updating);
  const updateSuccess = useAppSelector(state => state.activiteDept.updateSuccess);

  const handleClose = () => {
    navigate('/activite-dept');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProcesVerbals({}));
    dispatch(getEmployes({}));
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
    if (values.numAct !== undefined && typeof values.numAct !== 'number') {
      values.numAct = Number(values.numAct);
    }

    const entity = {
      ...activiteDeptEntity,
      ...values,
      procesVerbal: procesVerbals.find(it => it.id.toString() === values.procesVerbal?.toString()),
      employes: mapIdList(values.employes),
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
          ...activiteDeptEntity,
          procesVerbal: activiteDeptEntity?.procesVerbal?.id,
          employes: activiteDeptEntity?.employes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.activiteDept.home.createOrEditLabel" data-cy="ActiviteDeptCreateUpdateHeading">
            Créer ou éditer un Activite Dept
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
                <ValidatedField name="id" required readOnly id="activite-dept-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Num Act" id="activite-dept-numAct" name="numAct" data-cy="numAct" type="text" />
              <ValidatedField label="Type D" id="activite-dept-typeD" name="typeD" data-cy="typeD" type="text" />
              <ValidatedField label="Descript" id="activite-dept-descript" name="descript" data-cy="descript" type="text" />
              <ValidatedField label="Date Act" id="activite-dept-dateAct" name="dateAct" data-cy="dateAct" type="date" />
              <ValidatedField label="H Debut" id="activite-dept-hDebut" name="hDebut" data-cy="hDebut" type="date" />
              <ValidatedField label="H Fin" id="activite-dept-hFin" name="hFin" data-cy="hFin" type="date" />
              <ValidatedField
                label="Date Creation"
                id="activite-dept-dateCreation"
                name="dateCreation"
                data-cy="dateCreation"
                type="date"
              />
              <ValidatedField label="Createur" id="activite-dept-createur" name="createur" data-cy="createur" type="text" />
              <ValidatedField
                id="activite-dept-procesVerbal"
                name="procesVerbal"
                data-cy="procesVerbal"
                label="Proces Verbal"
                type="select"
              >
                <option value="" key="0" />
                {procesVerbals
                  ? procesVerbals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Employe" id="activite-dept-employe" data-cy="employe" type="select" multiple name="employes">
                <option value="" key="0" />
                {employes
                  ? employes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/activite-dept" replace color="info">
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

export default ActiviteDeptUpdate;
