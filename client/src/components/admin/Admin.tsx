import { useMemo } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthenticate from '../../hooks/useAuthenticate';
import { PagesEnum } from '../../types/enums/PagesEnum';
import { RolesEnum } from '../../types/enums/RolesEnum';
import Spinner from '../common/spinner/Spinner';

export default function Admin() {
  const { user, loading } = useAuthenticate(false);
  const desiredRole = useMemo(() => RolesEnum.USER, []);

  return (
    <>
      {!loading && user.role === desiredRole ? (
        <AdminPanel />
      ) : !loading && user.role !== desiredRole ? (
        <Navigate to={PagesEnum.Home} />
      ) : (
        <Spinner />
      )}
    </>
  );
}

function AdminPanel() {
  return <></>;
}
