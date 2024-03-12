import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableBlogs() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.blogs')}
      apiPathname="/blogs"
      create={false}
      delete={true}
      update={true}
    />
  );
}
