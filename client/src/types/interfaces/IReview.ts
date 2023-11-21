import { IUser } from './IUser';

export interface IReview {
  id: number;
  stars: number;
  text: string;
  user: IUser;
}
