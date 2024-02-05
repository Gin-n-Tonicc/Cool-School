import { IObjectWithId } from './IObjectWithId';

export interface IAnswer extends IObjectWithId {
  text: string;
  correct: boolean;
  question: number;
}
