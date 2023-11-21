import { ICategory } from './ICategory';
import { IFile } from './IFile';
import { IUser } from './IUser';

export interface ICourse {
  id: number;
  name: string;
  objectives: string;
  eligibility: string;
  stars: number;
  user: IUser;
  category: ICategory;
  picture: IFile;
}
