import AdminTable from '../admin-table/AdminTable';

// The component that displays the admin default table
export default function AdminTableDefault() {
  return (
    <AdminTable
      tableName=""
      list={[]}
      create={false}
      update={false}
      delete={false}
      onDelete={() => {}}
      onCreate={async () => true}
      onUpdate={async () => true}
    />
  );
}
