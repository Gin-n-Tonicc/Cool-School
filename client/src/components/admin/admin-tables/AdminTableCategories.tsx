import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

// The component that displays the admin categories table
export default function AdminTableCategories() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.categories')}
      apiPathname="/categories"
      create={true}
      delete={true}
      update={true}
    />
  );
}
