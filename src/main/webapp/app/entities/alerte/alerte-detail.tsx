import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alerte.reducer';

export const AlerteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alerteEntity = useAppSelector(state => state.alerte.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alerteDetailsHeading">Alerte</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{alerteEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{alerteEntity.type}</dd>
          <dt>
            <span id="delais">Delais</span>
          </dt>
          <dd>{alerteEntity.delais}</dd>
        </dl>
        <Button tag={Link} to="/alerte" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/alerte/${alerteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlerteDetail;
