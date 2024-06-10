import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './employe.reducer';

export const Employe = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const employeList = useAppSelector(state => state.employe.entities);
  const loading = useAppSelector(state => state.employe.loading);

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
      <h2 id="employe-heading" data-cy="EmployeHeading">
        Employes
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Actualiser la liste
          </Button>
          <Link to="/employe/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Créer un nouveau Employe
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {employeList && employeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('numEmploye')}>
                  Num Employe <FontAwesomeIcon icon={getSortIconByFieldName('numEmploye')} />
                </th>
                <th className="hand" onClick={sort('nom')}>
                  Nom <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                </th>
                <th className="hand" onClick={sort('prenom')}>
                  Prenom <FontAwesomeIcon icon={getSortIconByFieldName('prenom')} />
                </th>
                <th className="hand" onClick={sort('telIntern')}>
                  Tel Intern <FontAwesomeIcon icon={getSortIconByFieldName('telIntern')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  Email <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('niveau')}>
                  Niveau <FontAwesomeIcon icon={getSortIconByFieldName('niveau')} />
                </th>
                <th>
                  Agenda <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Activite Dept <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {employeList.map((employe, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/employe/${employe.id}`} color="link" size="sm">
                      {employe.id}
                    </Button>
                  </td>
                  <td>{employe.numEmploye}</td>
                  <td>{employe.nom}</td>
                  <td>{employe.prenom}</td>
                  <td>{employe.telIntern}</td>
                  <td>{employe.email}</td>
                  <td>{employe.niveau}</td>
                  <td>{employe.agenda ? <Link to={`/agenda/${employe.agenda.id}`}>{employe.agenda.id}</Link> : ''}</td>
                  <td>
                    {employe.activiteDepts
                      ? employe.activiteDepts.map((val, j) => (
                          <span key={j}>
                            <Link to={`/activite-dept/${val.id}`}>{val.numAct}</Link>
                            {j === employe.activiteDepts.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/employe/${employe.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Voir</span>
                      </Button>
                      <Button tag={Link} to={`/employe/${employe.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editer</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/employe/${employe.id}/delete`)}
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
          !loading && <div className="alert alert-warning">Aucun Employe trouvé</div>
        )}
      </div>
    </div>
  );
};

export default Employe;
