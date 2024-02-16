import { IObjectWithId } from '../common/IObjectWithId';

export interface IAnswer extends IObjectWithId {
  text: string;
  correct: boolean;
  question: number;
}
