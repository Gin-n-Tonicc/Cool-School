import { PropsWithChildren, createContext, useContext, useState } from 'react';
import { v4 as uuidV4 } from 'uuid';
import { IError } from '../interfaces/IError';

const ErrorContext = createContext<null | ErrorContextType>(null);
const maxErrors = 3;

type ErrorContextType = {
  errors: IError[];
  addError: (error: Omit<IError, 'id'>) => void;
  deleteError: (errorId: IError['id']) => void;
  clearErrors: () => void;
};

export const ErrorProvider = ({ children }: PropsWithChildren) => {
  const [errors, setErrors] = useState<ErrorContextType['errors']>([]);

  const addError: ErrorContextType['addError'] = (error) => {
    const newError = { ...error, id: `error-${uuidV4()}` };

    setErrors((errors) => {
      if (errors.length >= maxErrors) {
        return [
          ...errors.slice(errors.length - maxErrors + 1, maxErrors),
          newError,
        ];
      }

      return [...errors, newError];
    });
  };

  const deleteError: ErrorContextType['deleteError'] = (errorId) => {
    setErrors((errors) => errors.filter((x) => x.id !== errorId));
  };

  const clearErrors: ErrorContextType['clearErrors'] = () => {
    setErrors([]);
  };

  const value = { errors, addError, deleteError, clearErrors };

  return (
    <ErrorContext.Provider value={value}>{children}</ErrorContext.Provider>
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
