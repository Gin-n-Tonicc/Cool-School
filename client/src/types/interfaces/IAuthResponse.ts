import { IAuthRefreshResponse } from './IAuthRefreshResponse';
import { IUser } from './IUser';

export interface IAuthResponse extends IAuthRefreshResponse {
  user: IUser;
}
