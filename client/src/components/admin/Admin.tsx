import { useMemo } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import useAuthenticate from '../../hooks/useAuthenticate';
import { PagesEnum } from '../../types/enums/PagesEnum';
import { RolesEnum } from '../../types/enums/RolesEnum';
import Spinner from '../common/spinner/Spinner';

export default function Admin() {
  // Authenticate user
  const { user, loading } = useAuthenticate(false);
  const desiredRole = useMemo(() => RolesEnum.ADMIN, []);

  // If the user isn't an admin redirect to home
  // otherwise show the admin panel
  // show spinner if loading
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
