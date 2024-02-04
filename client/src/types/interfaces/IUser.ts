import { RolesEnum } from '../enums/RolesEnum';
import { IObjectWithId } from './IObjectWithId';

export interface IUser extends IObjectWithId {
  firstname: string;
  email: string;
  username: string;
  role: RolesEnum;
  description: string;
  additionalInfoRequired: boolean;
}
