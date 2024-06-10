import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgenda } from 'app/shared/model/agenda.model';
import { getEntities as getAgenda } from 'app/entities/agenda/agenda.reducer';
import { IActiviteDept } from 'app/shared/model/activite-dept.model';
import { getEntities as getActiviteDepts } from 'app/entities/activite-dept/activite-dept.reducer';
import { IEmploye } from 'app/shared/model/employe.model';
import { getEntity, updateEntity, createEntity, reset } from './employe.reducer';

export const EmployeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agenda = useAppSelector(state => state.agenda.entities);
  const activiteDepts = useAppSelector(state => state.activiteDept.entities);
  const employeEntity = useAppSelector(state => state.employe.entity);
  const loading = useAppSelector(state => state.employe.loading);
  const updating = useAppSelector(state => state.employe.updating);
  const updateSuccess = useAppSelector(state => state.employe.updateSuccess);

  const handleClose = () => {
    navigate('/employe');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgenda({}));
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
    if (values.numEmploye !== undefined && typeof values.numEmploye !== 'number') {
      values.numEmploye = Number(values.numEmploye);
    }
    if (values.niveau !== undefined && typeof values.niveau !== 'number') {
      values.niveau = Number(values.niveau);
    }

    const entity = {
      ...employeEntity,
      ...values,
      agenda: agenda.find(it => it.id.toString() === values.agenda?.toString()),
      activiteDepts: mapIdList(values.activiteDepts),
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
          ...employeEntity,
          agenda: employeEntity?.agenda?.id,
          activiteDepts: employeEntity?.activiteDepts?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.employe.home.createOrEditLabel" data-cy="EmployeCreateUpdateHeading">
            Créer ou éditer un Employe
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="employe-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Num Employe" id="employe-numEmploye" name="numEmploye" data-cy="numEmploye" type="text" />
              <ValidatedField label="Nom" id="employe-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField label="Prenom" id="employe-prenom" name="prenom" data-cy="prenom" type="text" />
              <ValidatedField label="Tel Intern" id="employe-telIntern" name="telIntern" data-cy="telIntern" type="text" />
              <ValidatedField label="Email" id="employe-email" name="email" data-cy="email" type="text" />
              <ValidatedField label="Niveau" id="employe-niveau" name="niveau" data-cy="niveau" type="text" />
              <ValidatedField id="employe-agenda" name="agenda" data-cy="agenda" label="Agenda" type="select">
                <option value="" key="0" />
                {agenda
                  ? agenda.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label="Activite Dept"
                id="employe-activiteDept"
                data-cy="activiteDept"
                type="select"
                multiple
                name="activiteDepts"
              >
                <option value="" key="0" />
                {activiteDepts
                  ? activiteDepts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.numAct}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employe" replace color="info">
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

export default EmployeUpdate;
