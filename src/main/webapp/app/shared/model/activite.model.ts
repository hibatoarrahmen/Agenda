import dayjs from 'dayjs';
import { IAlerte } from 'app/shared/model/alerte.model';

export interface IActivite {
  id?: number;
  numActivite?: number | null;
  typeA?: string | null;
  description?: string | null;
  dateAct?: dayjs.Dayjs | null;
  hDebut?: dayjs.Dayjs | null;
  hFin?: dayjs.Dayjs | null;
  dateCreation?: dayjs.Dayjs | null;
  createur?: string | null;
  visible?: number | null;
  alerte?: IAlerte | null;
}

export const defaultValue: Readonly<IActivite> = {};
