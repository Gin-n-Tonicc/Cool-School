import { IObjectWithId } from '../common/IObjectWithId';

export interface ICourseSubsection extends IObjectWithId {
  title: string;
  description: string;
  resources: any[];
  courseId: number;
}
