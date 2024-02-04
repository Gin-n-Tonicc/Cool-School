import { ICategory } from './ICategory';
import { IFile } from './IFile';
import { IObjectWithId } from './IObjectWithId';
import { IUser } from './IUser';

export interface IBlog extends IObjectWithId {
  title: string;
  content: string;
  summary: string;
  created_at: string;
  picture: IFile;
  owner: IUser;
  category: ICategory;
  commentCount: number;
  liked_users: IUser[];
}
