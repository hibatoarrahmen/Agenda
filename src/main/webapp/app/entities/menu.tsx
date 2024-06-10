import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/employe">
        Employe
      </MenuItem>
      <MenuItem icon="asterisk" to="/departement">
        Departement
      </MenuItem>
      <MenuItem icon="asterisk" to="/agenda-dept">
        Agenda Dept
      </MenuItem>
      <MenuItem icon="asterisk" to="/activite-dept">
        Activite Dept
      </MenuItem>
      <MenuItem icon="asterisk" to="/proces-verbal">
        Proces Verbal
      </MenuItem>
      <MenuItem icon="asterisk" to="/agenda">
        Agenda
      </MenuItem>
      <MenuItem icon="asterisk" to="/activite">
        Activite
      </MenuItem>
      <MenuItem icon="asterisk" to="/alerte">
        Alerte
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
