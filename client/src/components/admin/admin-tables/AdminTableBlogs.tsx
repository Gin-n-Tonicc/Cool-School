import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableBlogs() {
  return (
    <AdminTableApi
      tableName="Blogs"
      apiPathname="/blogs"
      create={false}
      delete={true}
      update={true}
    />
  );
}
