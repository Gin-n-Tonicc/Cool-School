import { IObjectWithId } from './IObjectWithId';
import { IUser } from './IUser';

export interface IReview extends IObjectWithId {
  stars: number;
  text: string;
  user: IUser;
}
