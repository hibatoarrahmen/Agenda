import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agenda.reducer';

export const AgendaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agendaEntity = useAppSelector(state => state.agenda.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agendaDetailsHeading">Agenda</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{agendaEntity.id}</dd>
          <dt>
            <span id="numAgenda">Num Agenda</span>
          </dt>
          <dd>{agendaEntity.numAgenda}</dd>
          <dt>
            <span id="dateCreation">Date Creation</span>
          </dt>
          <dd>
            {agendaEntity.dateCreation ? <TextFormat value={agendaEntity.dateCreation} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>Activite</dt>
          <dd>{agendaEntity.activite ? agendaEntity.activite.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/agenda" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agenda/${agendaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgendaDetail;
