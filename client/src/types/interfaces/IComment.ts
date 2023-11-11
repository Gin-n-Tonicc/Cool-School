import { IUser } from './IUser';

export interface IComment {
  id: number;
  comment: string;
  created_at: string;
  ownerId: number;
  blogId: number;
  liked_users: IUser[];
  owner: IUser;
}
