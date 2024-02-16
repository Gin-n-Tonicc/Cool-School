import { IUser } from '../auth/IUser';
import { IObjectWithId } from '../common/IObjectWithId';

export interface IReview extends IObjectWithId {
  stars: number;
  text: string;
  user: IUser;
}
