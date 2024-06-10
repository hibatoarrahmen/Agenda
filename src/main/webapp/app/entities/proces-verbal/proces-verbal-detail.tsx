import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './proces-verbal.reducer';

export const ProcesVerbalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const procesVerbalEntity = useAppSelector(state => state.procesVerbal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="procesVerbalDetailsHeading">Proces Verbal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{procesVerbalEntity.id}</dd>
          <dt>
            <span id="numProcesV">Num Proces V</span>
          </dt>
          <dd>{procesVerbalEntity.numProcesV}</dd>
          <dt>
            <span id="resum">Resum</span>
          </dt>
          <dd>{procesVerbalEntity.resum}</dd>
        </dl>
        <Button tag={Link} to="/proces-verbal" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/proces-verbal/${procesVerbalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProcesVerbalDetail;
