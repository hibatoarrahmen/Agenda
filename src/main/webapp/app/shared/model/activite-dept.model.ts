import dayjs from 'dayjs';
import { IProcesVerbal } from 'app/shared/model/proces-verbal.model';
import { IEmploye } from 'app/shared/model/employe.model';

export interface IActiviteDept {
  id?: number;
  numAct?: number | null;
  typeD?: string | null;
  descript?: string | null;
  dateAct?: dayjs.Dayjs | null;
  hDebut?: dayjs.Dayjs | null;
  hFin?: dayjs.Dayjs | null;
  dateCreation?: dayjs.Dayjs | null;
  createur?: string | null;
  procesVerbal?: IProcesVerbal | null;
  employes?: IEmploye[] | null;
}

export const defaultValue: Readonly<IActiviteDept> = {};
