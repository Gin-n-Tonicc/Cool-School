import { IUser } from '../auth/IUser';
import { IObjectWithId } from '../common/IObjectWithId';

export interface IComment extends IObjectWithId {
  comment: string;
  created_at: string;
  ownerId: number;
  blogId: number;
  liked_users: IUser[];
  owner: IUser;
}
