import { useMemo } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthenticate from '../../hooks/useAuthenticate';
import { PagesEnum } from '../../types/enums/PagesEnum';
import { RolesEnum } from '../../types/enums/RolesEnum';
import Spinner from '../common/spinner/Spinner';
import AdminTableApi from './admin-table-api/AdminTableApi';

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
        <AdminTableApi
          tableName="Users"
          apiPathname="/users"
          create={false}
          delete={true}
          update={true}
        />
      </div>
    </>
  );
}
