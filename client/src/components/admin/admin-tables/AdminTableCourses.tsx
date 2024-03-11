import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableCourses() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.courses')}
      apiPathname="/courses"
      create={false}
      delete={true}
      update={true}
    />
  );
}
