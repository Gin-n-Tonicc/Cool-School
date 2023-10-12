import { PropsWithChildren, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useErrorContext } from '../../contexts/ErrorContext';

export default function NavigationMiddleware({ children }: PropsWithChildren) {
  const location = useLocation();
  const { clearErrors } = useErrorContext();

  useEffect(() => {
    clearErrors();
  }, [location.pathname, clearErrors]);

  return <>{children}</>;
}
