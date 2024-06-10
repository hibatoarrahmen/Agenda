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

import { getEntities } from './activite-dept.reducer';

export const ActiviteDept = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const activiteDeptList = useAppSelector(state => state.activiteDept.entities);
  const loading = useAppSelector(state => state.activiteDept.loading);

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
      <h2 id="activite-dept-heading" data-cy="ActiviteDeptHeading">
        ActiviteDepts
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/activite-dept/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Activite Dept
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {activiteDeptList && activiteDeptList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('numAct')}>
                  Num Act <FontAwesomeIcon icon={getSortIconByFieldName('numAct')} />
                </th>
                <th className="hand" onClick={sort('typeD')}>
                  Type D <FontAwesomeIcon icon={getSortIconByFieldName('typeD')} />
                </th>
                <th className="hand" onClick={sort('descript')}>
                  Descript <FontAwesomeIcon icon={getSortIconByFieldName('descript')} />
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
                <th>
                  Proces Verbal <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Employe <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {activiteDeptList.map((activiteDept, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/activite-dept/${activiteDept.id}`} color="link" size="sm">
                      {activiteDept.id}
                    </Button>
                  </td>
                  <td>{activiteDept.numAct}</td>
                  <td>{activiteDept.typeD}</td>
                  <td>{activiteDept.descript}</td>
                  <td>
                    {activiteDept.dateAct ? <TextFormat type="date" value={activiteDept.dateAct} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {activiteDept.hDebut ? <TextFormat type="date" value={activiteDept.hDebut} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{activiteDept.hFin ? <TextFormat type="date" value={activiteDept.hFin} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {activiteDept.dateCreation ? (
                      <TextFormat type="date" value={activiteDept.dateCreation} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{activiteDept.createur}</td>
                  <td>
                    {activiteDept.procesVerbal ? (
                      <Link to={`/proces-verbal/${activiteDept.procesVerbal.id}`}>{activiteDept.procesVerbal.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {activiteDept.employes
                      ? activiteDept.employes.map((val, j) => (
                          <span key={j}>
                            <Link to={`/employe/${val.id}`}>{val.id}</Link>
                            {j === activiteDept.employes.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/activite-dept/${activiteDept.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button tag={Link} to={`/activite-dept/${activiteDept.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/activite-dept/${activiteDept.id}/delete`)}
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
          !loading && <div className="alert alert-warning">Aucun Activite Dept trouvé</div>
        )}
      </div>
    </div>
  );
};

export default ActiviteDept;
