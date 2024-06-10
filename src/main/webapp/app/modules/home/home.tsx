import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">Bienvenue dans notre application de gestion Agenda realise par Hiba Ghrab et Malek Ben Kemla </h1>
        <p className="lead">
          Projet de génie logiciel : agenda avec Spring Boot, React, Docker et JHipster. Backend MySQL robuste, frontend React interactif,
          déploiement facile avec Docker
        </p>
        {account?.login ? (
          <div>
            <Alert color="success">Vous êtes connecté en tant que &quot;{account.login}&quot;.</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              Si vous voulez vous
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                connecter
              </Link>
              , vous pouvez utiliser les comptes par défaut : <br /> - Administrateur (nom d&apos;utilisateur=&quot;admin&quot; et mot de
              passe =&quot;admin&quot;) <br /> - Utilisateur (nom d&apos;utilisateur=&quot;user&quot; et mot de passe =&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              Vous n&apos;avez pas encore de compte ?&nbsp;
              <Link to="/account/register" className="alert-link">
                Créer un compte
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
