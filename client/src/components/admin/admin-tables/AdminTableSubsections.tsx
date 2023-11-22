import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableSubsections() {
  return (
    <AdminTableApi
      tableName="Subsections"
      apiPathname="/courseSubsections"
      create={false}
      delete={true}
      update={true}
    />
  );
}
