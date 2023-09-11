import { Navigate, Outlet } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';

export default function ProtectedRoute({ onlyUser }: { onlyUser: boolean }) {
  const { isAuthenticated } = useAuthContext();

  if (isAuthenticated != onlyUser) {
    return <Navigate to={'/'} replace />;
  }

  return <Outlet />;
}
