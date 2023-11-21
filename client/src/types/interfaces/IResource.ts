import { ICourseSubsection } from './ICourseSubsection';
import { IFile } from './IFile';

export interface IResource {
  id: number;
  name: string;
  file: IFile;
  subsection: ICourseSubsection;
}
