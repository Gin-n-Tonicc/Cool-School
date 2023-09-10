import { IUser } from './IUser';

export type IAuthStorage = {
  accessToken: string;
  refreshToken: string;
} & IUser;
