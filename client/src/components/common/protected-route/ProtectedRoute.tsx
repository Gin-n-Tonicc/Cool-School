import { useCallback } from 'react';
import { Navigate, Outlet, To, useLocation } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';

const logoutPath = '/logout';
const loginPath = '/login';

type ProtectedRouteProps = {
  onlyUser: boolean;
};

export default function ProtectedRoute({ onlyUser }: ProtectedRouteProps) {
  const { isAuthenticated } = useAuthContext();
  const { pathname } = useLocation();

  const generateNavPath = useCallback(
    (path: string) => {
      const navPath: To = {
        pathname: path,
        search: `?redirect=${pathname}`,
      };
      return navPath;
    },
    [pathname]
  );

  const passThrew = isAuthenticated === onlyUser;

  if (onlyUser && !passThrew && pathname !== logoutPath) {
    return <Navigate to={generateNavPath(loginPath)} />;
  }

  if (!passThrew) {
    return <Navigate to={'/'} />;
  }

  return <Outlet />;
}
