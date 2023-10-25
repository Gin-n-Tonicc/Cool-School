import { useMemo } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import useAuthenticate from '../../hooks/useAuthenticate';
import { PagesEnum } from '../../types/enums/PagesEnum';
import { RolesEnum } from '../../types/enums/RolesEnum';
import Spinner from '../common/spinner/Spinner';

export default function Admin() {
  const { user, loading } = useAuthenticate(false);
  const desiredRole = useMemo(() => RolesEnum.ADMIN, []);

  return (
    <>
      {!loading && user.role === desiredRole ? (
        <AdminView />
      ) : !loading && user.role !== desiredRole ? (
        <Navigate to={PagesEnum.Home} />
      ) : (
        <Spinner />
      )}
    </>
  );
}

// ----------------------------------------------------------------------

function AdminView() {
  return (
    <>
      <div className="container">
        <Outlet />
      </div>
    </>
  );
}
