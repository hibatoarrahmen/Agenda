import { IAgendaDept } from 'app/shared/model/agenda-dept.model';
import { IEmploye } from 'app/shared/model/employe.model';

export interface IDepartement {
  id?: number;
  num?: number | null;
  nom?: string | null;
  agendaDept?: IAgendaDept | null;
  employe?: IEmploye | null;
}

export const defaultValue: Readonly<IDepartement> = {};
