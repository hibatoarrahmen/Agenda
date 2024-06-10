import { IAgenda } from 'app/shared/model/agenda.model';
import { IActiviteDept } from 'app/shared/model/activite-dept.model';

export interface IEmploye {
  id?: number;
  numEmploye?: number | null;
  nom?: string | null;
  prenom?: string | null;
  telIntern?: string | null;
  email?: string | null;
  niveau?: number | null;
  agenda?: IAgenda | null;
  activiteDepts?: IActiviteDept[] | null;
}

export const defaultValue: Readonly<IEmploye> = {};
