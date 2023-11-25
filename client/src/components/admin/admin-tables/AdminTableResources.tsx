import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableResources() {
  return (
    <AdminTableApi
      tableName="Resources"
      apiPathname="/resources"
      create={false}
      delete={true}
      update={true}
    />
  );
}
