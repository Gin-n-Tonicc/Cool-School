import { ICategory } from './ICategory';
import { IFile } from './IFile';
import { IUser } from './IUser';

export interface IBlog {
  id: number;
  title: string;
  content: string;
  summary: string;
  created_at: string;
  picture: IFile;
  owner: IUser;
  category: ICategory;
}
