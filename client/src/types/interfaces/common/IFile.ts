import { IObjectWithId } from './IObjectWithId';

export interface IFile extends IObjectWithId {
  name: string;
  url: string;
  type: string;
}
