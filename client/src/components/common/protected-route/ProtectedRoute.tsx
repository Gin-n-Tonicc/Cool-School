import { useCallback } from 'react';
import { Navigate, Outlet, To, useLocation } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';

const logoutPath = '/logout';
const loginPath = '/login';

type ProtectedRouteProps = {
  onlyUser: boolean;
  onlyTeacher?: boolean;
};

// The component that protects a route based on the user data
// whether he is logged or not, whether he is a teacher or not, etc.
export default function ProtectedRoute({
  onlyUser,
  onlyTeacher,
}: ProtectedRouteProps) {
  const { isTeacher, isAuthenticated } = useAuthContext();
  const { pathname } = useLocation();

  // Attach redirectTo search param
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

  const passThrough = isAuthenticated === onlyUser;

  if (!onlyTeacher && onlyUser && !passThrough && pathname !== logoutPath) {
    return <Navigate to={generateNavPath(loginPath)} />;
  }

  if (
    !passThrough ||
    (onlyTeacher !== undefined && onlyTeacher !== isTeacher)
  ) {
    return <Navigate to={PagesEnum.Home} />;
  }

  return <Outlet />;
}
