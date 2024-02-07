import { IUserQuizProgress } from './IUserQuizProgress';

export interface ISaveUserProgress extends IUserQuizProgress {
  attemptId: number;
}
