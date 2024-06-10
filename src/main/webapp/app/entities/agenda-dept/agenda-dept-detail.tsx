import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agenda-dept.reducer';

export const AgendaDeptDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agendaDeptEntity = useAppSelector(state => state.agendaDept.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agendaDeptDetailsHeading">Agenda Dept</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{agendaDeptEntity.id}</dd>
          <dt>
            <span id="numAgenda">Num Agenda</span>
          </dt>
          <dd>{agendaDeptEntity.numAgenda}</dd>
          <dt>
            <span id="dateMAJ">Date MAJ</span>
          </dt>
          <dd>
            {agendaDeptEntity.dateMAJ ? <TextFormat value={agendaDeptEntity.dateMAJ} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>Activite Dept</dt>
          <dd>{agendaDeptEntity.activiteDept ? agendaDeptEntity.activiteDept.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/agenda-dept" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agenda-dept/${agendaDeptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgendaDeptDetail;
