import { ICourseSubsection } from '../courses/ICourseSubsection';
import { IFile } from './IFile';
import { IObjectWithId } from './IObjectWithId';

export interface IResource extends IObjectWithId {
  name: string;
  file: IFile;
  subsection: ICourseSubsection;
}
