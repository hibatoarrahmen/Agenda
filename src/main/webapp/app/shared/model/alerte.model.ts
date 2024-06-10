export interface IAlerte {
  id?: number;
  type?: string | null;
  delais?: number | null;
}

export const defaultValue: Readonly<IAlerte> = {};
