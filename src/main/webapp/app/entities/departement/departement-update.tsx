import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgendaDept } from 'app/shared/model/agenda-dept.model';
import { getEntities as getAgendaDepts } from 'app/entities/agenda-dept/agenda-dept.reducer';
import { IEmploye } from 'app/shared/model/employe.model';
import { getEntities as getEmployes } from 'app/entities/employe/employe.reducer';
import { IDepartement } from 'app/shared/model/departement.model';
import { getEntity, updateEntity, createEntity, reset } from './departement.reducer';

export const DepartementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agendaDepts = useAppSelector(state => state.agendaDept.entities);
  const employes = useAppSelector(state => state.employe.entities);
  const departementEntity = useAppSelector(state => state.departement.entity);
  const loading = useAppSelector(state => state.departement.loading);
  const updating = useAppSelector(state => state.departement.updating);
  const updateSuccess = useAppSelector(state => state.departement.updateSuccess);

  const handleClose = () => {
    navigate('/departement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgendaDepts({}));
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
    if (values.num !== undefined && typeof values.num !== 'number') {
      values.num = Number(values.num);
    }

    const entity = {
      ...departementEntity,
      ...values,
      agendaDept: agendaDepts.find(it => it.id.toString() === values.agendaDept?.toString()),
      employe: employes.find(it => it.id.toString() === values.employe?.toString()),
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
          ...departementEntity,
          agendaDept: departementEntity?.agendaDept?.id,
          employe: departementEntity?.employe?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaApp.departement.home.createOrEditLabel" data-cy="DepartementCreateUpdateHeading">
            Créer ou éditer un Departement
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="departement-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Num" id="departement-num" name="num" data-cy="num" type="text" />
              <ValidatedField label="Nom" id="departement-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField id="departement-agendaDept" name="agendaDept" data-cy="agendaDept" label="Agenda Dept" type="select">
                <option value="" key="0" />
                {agendaDepts
                  ? agendaDepts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="departement-employe" name="employe" data-cy="employe" label="Employe" type="select">
                <option value="" key="0" />
                {employes
                  ? employes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/departement" replace color="info">
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

export default DepartementUpdate;
