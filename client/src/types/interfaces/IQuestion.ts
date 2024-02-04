import { IObjectWithId } from './IObjectWithId';

export interface IQuestion extends IObjectWithId {
  marks: number;
  description: string;
  quiz: number;
}
