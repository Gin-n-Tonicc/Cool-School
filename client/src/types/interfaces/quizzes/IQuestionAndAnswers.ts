import { IAnswer } from './IAnswer';
import { IQuestion } from './IQuestion';

export interface IQuestionAndAnswers {
  question: IQuestion;
  answers: IAnswer[];
}
