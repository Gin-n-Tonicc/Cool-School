import { IObjectWithId } from './IObjectWithId';
import { IUser } from './IUser';

export interface IComment extends IObjectWithId {
  comment: string;
  created_at: string;
  ownerId: number;
  blogId: number;
  liked_users: IUser[];
  owner: IUser;
}
