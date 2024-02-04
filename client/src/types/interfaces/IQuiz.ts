import { IObjectWithId } from './IObjectWithId';

export interface IQuiz extends IObjectWithId {
  title: string;
  description: string;
  startTime: Date;
  endTime: Date;
  subsectionId: number;
  attemptLimit: number;
  quizDurationInMinutes: number;
}
