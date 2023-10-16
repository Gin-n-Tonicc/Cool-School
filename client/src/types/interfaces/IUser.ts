import { RolesEnum } from '../enums/RolesEnum';

export interface IUser {
  id: number;
  firstname: string;
  email: string;
  username: string;
  role: RolesEnum;
}
