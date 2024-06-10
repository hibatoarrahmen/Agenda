import dayjs from 'dayjs';
import { IActiviteDept } from 'app/shared/model/activite-dept.model';

export interface IAgendaDept {
  id?: number;
  numAgenda?: number | null;
  dateMAJ?: dayjs.Dayjs | null;
  activiteDept?: IActiviteDept | null;
}

export const defaultValue: Readonly<IAgendaDept> = {};
