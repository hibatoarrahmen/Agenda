import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './departement.reducer';

export const DepartementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const departementEntity = useAppSelector(state => state.departement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="departementDetailsHeading">Departement</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{departementEntity.id}</dd>
          <dt>
            <span id="num">Num</span>
          </dt>
          <dd>{departementEntity.num}</dd>
          <dt>
            <span id="nom">Nom</span>
          </dt>
          <dd>{departementEntity.nom}</dd>
          <dt>Agenda Dept</dt>
          <dd>{departementEntity.agendaDept ? departementEntity.agendaDept.id : ''}</dd>
          <dt>Employe</dt>
          <dd>{departementEntity.employe ? departementEntity.employe.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/departement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/departement/${departementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DepartementDetail;
