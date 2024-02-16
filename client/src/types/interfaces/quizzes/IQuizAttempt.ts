import { IUser } from '../auth/IUser';
import { IObjectWithId } from '../common/IObjectWithId';
import { IQuiz } from './IQuiz';

export interface IQuizAttempt extends IObjectWithId {
  quiz: IQuiz;
  user: IUser;
  answers: any[];
  totalMarks: number;
  attemptNumber: number;
  timeLeft: number;
  completed: boolean;
  remainingTimeInSeconds: number;
}
