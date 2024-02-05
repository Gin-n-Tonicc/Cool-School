import { IObjectWithId } from '../common/IObjectWithId';

export interface IQuestion extends IObjectWithId {
  marks: number;
  description: string;
  quiz: number;
}
