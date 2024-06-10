import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './activite.reducer';

export const Activite = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const activiteList = useAppSelector(state => state.activite.entities);
  const loading = useAppSelector(state => state.activite.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="activite-heading" data-cy="ActiviteHeading">
        Activites
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/activite/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Activite
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {activiteList && activiteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('numActivite')}>
                  Num Activite <FontAwesomeIcon icon={getSortIconByFieldName('numActivite')} />
                </th>
                <th className="hand" onClick={sort('typeA')}>
                  Type A <FontAwesomeIcon icon={getSortIconByFieldName('typeA')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  Description <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('dateAct')}>
                  Date Act <FontAwesomeIcon icon={getSortIconByFieldName('dateAct')} />
                </th>
                <th className="hand" onClick={sort('hDebut')}>
                  H Debut <FontAwesomeIcon icon={getSortIconByFieldName('hDebut')} />
                </th>
                <th className="hand" onClick={sort('hFin')}>
                  H Fin <FontAwesomeIcon icon={getSortIconByFieldName('hFin')} />
                </th>
                <th className="hand" onClick={sort('dateCreation')}>
                  Date Creation <FontAwesomeIcon icon={getSortIconByFieldName('dateCreation')} />
                </th>
                <th className="hand" onClick={sort('createur')}>
                  Createur <FontAwesomeIcon icon={getSortIconByFieldName('createur')} />
                </th>
                <th className="hand" onClick={sort('visible')}>
                  Visible <FontAwesomeIcon icon={getSortIconByFieldName('visible')} />
                </th>
                <th>
                  Alerte <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activiteList.map((activite, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/activite/${activite.id}`} color="link" size="sm">
                      {activite.id}
                    </Button>
                  </td>
                  <td>{activite.numActivite}</td>
                  <td>{activite.typeA}</td>
                  <td>{activite.description}</td>
                  <td>{activite.dateAct ? <TextFormat type="date" value={activite.dateAct} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{activite.hDebut ? <TextFormat type="date" value={activite.hDebut} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{activite.hFin ? <TextFormat type="date" value={activite.hFin} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {activite.dateCreation ? <TextFormat type="date" value={activite.dateCreation} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{activite.createur}</td>
                  <td>{activite.visible}</td>
                  <td>{activite.alerte ? <Link to={`/alerte/${activite.alerte.id}`}>{activite.alerte.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/activite/${activite.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button tag={Link} to={`/activite/${activite.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/activite/${activite.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Supprimer</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Aucun Activite trouvé</div>
        )}
      </div>
    </div>
  );
};

export default Activite;
