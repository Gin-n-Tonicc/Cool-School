import { IObjectWithId } from '../common/IObjectWithId';

export interface IQuiz extends IObjectWithId {
  title: string;
  description: string;
  startTime: Date;
  endTime: Date;
  subsectionId: number;
  attemptLimit: number;
  totalMarks: number;
  quizDurationInMinutes: number;
  courseId?: number;
}
