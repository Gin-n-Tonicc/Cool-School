import { ICategory } from './ICategory';
import { IFile } from './IFile';
import { IObjectWithId } from './IObjectWithId';
import { IUser } from './IUser';

export interface ICourse extends IObjectWithId {
  name: string;
  objectives: string;
  eligibility: string;
  stars: number;
  user: IUser;
  category: ICategory;
  picture: IFile;
}
