import { IObjectWithId } from '../common/IObjectWithId';

export interface IUserQuizProgress extends IObjectWithId {
  questionId: number;
  answerId: number;
  quizId: number;
  userId: number;
}
