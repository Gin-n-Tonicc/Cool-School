import { IObjectWithId } from './IObjectWithId';

export interface ICourseSubsection extends IObjectWithId {
  title: string;
  description: string;
  resources: any[];
  courseId: number;
}
