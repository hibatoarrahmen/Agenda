import dayjs from 'dayjs';
import { IActivite } from 'app/shared/model/activite.model';

export interface IAgenda {
  id?: number;
  numAgenda?: number | null;
  dateCreation?: dayjs.Dayjs | null;
  activite?: IActivite | null;
}

export const defaultValue: Readonly<IAgenda> = {};
