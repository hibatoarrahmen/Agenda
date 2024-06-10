import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activite-dept.reducer';

export const ActiviteDeptDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activiteDeptEntity = useAppSelector(state => state.activiteDept.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activiteDeptDetailsHeading">Activite Dept</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activiteDeptEntity.id}</dd>
          <dt>
            <span id="numAct">Num Act</span>
          </dt>
          <dd>{activiteDeptEntity.numAct}</dd>
          <dt>
            <span id="typeD">Type D</span>
          </dt>
          <dd>{activiteDeptEntity.typeD}</dd>
          <dt>
            <span id="descript">Descript</span>
          </dt>
          <dd>{activiteDeptEntity.descript}</dd>
          <dt>
            <span id="dateAct">Date Act</span>
          </dt>
          <dd>
            {activiteDeptEntity.dateAct ? (
              <TextFormat value={activiteDeptEntity.dateAct} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="hDebut">H Debut</span>
          </dt>
          <dd>
            {activiteDeptEntity.hDebut ? <TextFormat value={activiteDeptEntity.hDebut} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="hFin">H Fin</span>
          </dt>
          <dd>
            {activiteDeptEntity.hFin ? <TextFormat value={activiteDeptEntity.hFin} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateCreation">Date Creation</span>
          </dt>
          <dd>
            {activiteDeptEntity.dateCreation ? (
              <TextFormat value={activiteDeptEntity.dateCreation} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createur">Createur</span>
          </dt>
          <dd>{activiteDeptEntity.createur}</dd>
          <dt>Proces Verbal</dt>
          <dd>{activiteDeptEntity.procesVerbal ? activiteDeptEntity.procesVerbal.id : ''}</dd>
          <dt>Employe</dt>
          <dd>
            {activiteDeptEntity.employes
              ? activiteDeptEntity.employes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {activiteDeptEntity.employes && i === activiteDeptEntity.employes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/activite-dept" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Retour</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activite-dept/${activiteDeptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActiviteDeptDetail;
