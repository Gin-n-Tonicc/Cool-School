import { useMemo } from 'react';
import { Navigate } from 'react-router-dom';
import useAuthenticate from '../../hooks/useAuthenticate';
import { PagesEnum } from '../../types/enums/PagesEnum';
import { RolesEnum } from '../../types/enums/RolesEnum';
import Spinner from '../common/spinner/Spinner';
import AdminTable from './admin-table/AdminTable';

export default function Admin() {
  const { user, loading } = useAuthenticate(false);
  const desiredRole = useMemo(() => RolesEnum.USER, []);

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
  const mockedUsers2 = [...Array(10)].map((_, i) => ({
    username: `username${i + 1}`,
    id: i + 1,
    firstname: `firstname${i + 1}`,
    email: `email${i + 1}`,
    role: RolesEnum.USER,
  }));

  return (
    <>
      <div className="container">
        <AdminTable tableName="Users" list={mockedUsers2} />
      </div>
    </>
  );
}
