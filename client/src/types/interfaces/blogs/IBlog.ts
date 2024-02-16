import { IUser } from '../auth/IUser';
import { ICategory } from '../common/ICategory';
import { IFile } from '../common/IFile';
import { IObjectWithId } from '../common/IObjectWithId';

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
