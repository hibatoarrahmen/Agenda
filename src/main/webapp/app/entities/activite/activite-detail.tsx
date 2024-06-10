import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activite.reducer';

export const ActiviteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activiteEntity = useAppSelector(state => state.activite.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activiteDetailsHeading">Activite</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activiteEntity.id}</dd>
          <dt>
            <span id="numActivite">Num Activite</span>
          </dt>
          <dd>{activiteEntity.numActivite}</dd>
          <dt>
            <span id="typeA">Type A</span>
          </dt>
          <dd>{activiteEntity.typeA}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{activiteEntity.description}</dd>
          <dt>
            <span id="dateAct">Date Act</span>
          </dt>
          <dd>
            {activiteEntity.dateAct ? <TextFormat value={activiteEntity.dateAct} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="hDebut">H Debut</span>
          </dt>
          <dd>{activiteEntity.hDebut ? <TextFormat value={activiteEntity.hDebut} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="hFin">H Fin</span>
          </dt>
          <dd>{activiteEntity.hFin ? <TextFormat value={activiteEntity.hFin} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dateCreation">Date Creation</span>
          </dt>
          <dd>
            {activiteEntity.dateCreation ? (
              <TextFormat value={activiteEntity.dateCreation} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createur">Createur</span>
          </dt>
          <dd>{activiteEntity.createur}</dd>
          <dt>
            <span id="visible">Visible</span>
          </dt>
          <dd>{activiteEntity.visible}</dd>
          <dt>Alerte</dt>
          <dd>{activiteEntity.alerte ? activiteEntity.alerte.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/activite" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activite/${activiteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActiviteDetail;
