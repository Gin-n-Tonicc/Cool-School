import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableCourses() {
  return (
    <AdminTableApi
      tableName="Courses"
      apiPathname="/courses"
      create={false}
      delete={true}
      update={true}
    />
  );
}
