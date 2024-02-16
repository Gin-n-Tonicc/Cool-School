import { ErrorTypeEnum } from '../../enums/ErrorTypeEnum';

export interface IError {
  message: string;
  unmountAfter: number;
  id: string;
  errorType: ErrorTypeEnum;
}
