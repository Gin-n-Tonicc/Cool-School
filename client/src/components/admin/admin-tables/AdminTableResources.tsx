import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

export default function AdminTableResources() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.resources')}
      apiPathname="/resources"
      create={false}
      delete={true}
      update={true}
    />
  );
}
