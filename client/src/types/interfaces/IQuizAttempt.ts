import { IObjectWithId } from './IObjectWithId';
import { IQuiz } from './IQuiz';
import { IUser } from './IUser';

export interface IQuizAttempt extends IObjectWithId {
  quiz: IQuiz;
  user: IUser;
  answers: any[];
  totalMarks: number;
  attemptNumber: number;
  timeLeft: number;
  completed: boolean;
}
