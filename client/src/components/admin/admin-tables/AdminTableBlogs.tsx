import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableBlogs() {
  return (
    <AdminTableApi
      tableName="Blogs"
      apiPathname="/blogs"
      create={true}
      delete={true}
      update={true}
    />
  );
}
