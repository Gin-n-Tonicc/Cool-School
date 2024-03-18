import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

// The component that displays the admin users table
export default function AdminTableUsers() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.users')}
      apiPathname="/users"
      create={false}
      delete={true}
      update={true}
    />
  );
}
