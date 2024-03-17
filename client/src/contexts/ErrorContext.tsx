import {
  PropsWithChildren,
  createContext,
  useCallback,
  useContext,
  useState,
} from 'react';
import { v4 as uuidV4 } from 'uuid';
import { ErrorTypeEnum } from '../types/enums/ErrorTypeEnum';
import { IError } from '../types/interfaces/common/IError';

const ErrorContext = createContext<null | ErrorContextType>(null);
const maxErrors = 3;
const unmountAfter = 10000;

type ErrorContextType = {
  errors: IError[];
  addError: (error: string, errorType?: ErrorTypeEnum) => void;
  deleteError: (errorId: IError['id']) => void;
  clearErrors: () => void;
};

// The component that provides all of the children
// with the necessary alert properties and functions
export const ErrorProvider = ({ children }: PropsWithChildren) => {
  const [errors, setErrors] = useState<ErrorContextType['errors']>([]);

  const addError: ErrorContextType['addError'] = useCallback(
    (error, errorType) => {
      // construct error
      const newError: IError = {
        message: error,
        unmountAfter: unmountAfter,
        id: `error-${uuidV4()}`,
        errorType: errorType || ErrorTypeEnum.EXCEPTION,
      };

      // If current alerts' length >= {n} (max length) remove the oldest alert
      // Add alert to the end of the list of alerts
      setErrors((errors) => {
        if (errors.length >= maxErrors) {
          return [
            ...errors.slice(errors.length - maxErrors + 1, maxErrors),
            newError,
          ];
        }

        return [...errors, newError];
      });
    },
    []
  );

  // Delete alert
  const deleteError: ErrorContextType['deleteError'] = useCallback(
    (errorId) => {
      setErrors((errors) => errors.filter((x) => x.id !== errorId));
    },
    []
  );

  // Clear all alerts
  const clearErrors: ErrorContextType['clearErrors'] = useCallback(() => {
    setErrors([]);
  }, []);

  return (
    <ErrorContext.Provider
      value={{ errors, addError, deleteError, clearErrors }}>
      {children}
    </ErrorContext.Provider>
  );
};

export const useErrorContext = () => {
  const errorContext = useContext(ErrorContext);

  if (!errorContext) {
    throw new Error(
      'useErrorContext has to be used within <ErrorContext.Provider>'
    );
  }

  return errorContext;
};
