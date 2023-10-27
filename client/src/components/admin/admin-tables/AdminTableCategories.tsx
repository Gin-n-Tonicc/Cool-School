import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableCategories() {
  return (
    <AdminTableApi
      tableName="Categories"
      apiPathname="/categories"
      create={true}
      delete={true}
      update={true}
    />
  );
}
