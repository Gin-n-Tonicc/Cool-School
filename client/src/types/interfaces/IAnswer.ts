import { IObjectWithId } from './IObjectWithId';

export interface IAnswer extends IObjectWithId {
  text: string;
  isCorrect: boolean;
  question: number;
}
