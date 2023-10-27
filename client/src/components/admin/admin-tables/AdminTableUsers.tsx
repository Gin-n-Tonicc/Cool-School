import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableUsers() {
  return (
    <AdminTableApi
      tableName="Users"
      apiPathname="/users"
      create={false}
      delete={true}
      update={true}
    />
  );
}
