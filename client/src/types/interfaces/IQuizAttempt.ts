import { IQuiz } from './IQuiz';
import { IUser } from './IUser';

export interface IQuizAttempt {
  quiz: IQuiz;
  user: IUser;
  answers: any[];
  totalMarks: number;
  attemptNumber: number;
  timeLeft: number;
  completed: boolean;
}
