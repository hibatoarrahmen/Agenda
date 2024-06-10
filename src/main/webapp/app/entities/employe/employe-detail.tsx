import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employe.reducer';

export const EmployeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeEntity = useAppSelector(state => state.employe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeDetailsHeading">Employe</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{employeEntity.id}</dd>
          <dt>
            <span id="numEmploye">Num Employe</span>
          </dt>
          <dd>{employeEntity.numEmploye}</dd>
          <dt>
            <span id="nom">Nom</span>
          </dt>
          <dd>{employeEntity.nom}</dd>
          <dt>
            <span id="prenom">Prenom</span>
          </dt>
          <dd>{employeEntity.prenom}</dd>
          <dt>
            <span id="telIntern">Tel Intern</span>
          </dt>
          <dd>{employeEntity.telIntern}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{employeEntity.email}</dd>
          <dt>
            <span id="niveau">Niveau</span>
          </dt>
          <dd>{employeEntity.niveau}</dd>
          <dt>Agenda</dt>
          <dd>{employeEntity.agenda ? employeEntity.agenda.id : ''}</dd>
          <dt>Activite Dept</dt>
          <dd>
            {employeEntity.activiteDepts
              ? employeEntity.activiteDepts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.numAct}</a>
                    {employeEntity.activiteDepts && i === employeEntity.activiteDepts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/employe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employe/${employeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeDetail;
