import { IDefaultObject } from './IDefaultObject';

export interface IObjectWithId extends IDefaultObject<any> {
  id: number;
}
