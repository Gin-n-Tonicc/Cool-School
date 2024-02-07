import { IUser } from '../auth/IUser';
import { ICategory } from '../common/ICategory';
import { IFile } from '../common/IFile';
import { IObjectWithId } from '../common/IObjectWithId';

export interface ICourse extends IObjectWithId {
  name: string;
  objectives: string;
  eligibility: string;
  stars: number;
  user: IUser;
  category: ICategory;
  picture: IFile;
}
