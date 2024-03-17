import { useTranslation } from 'react-i18next';
import AdminTableApi from '../admin-table-api/AdminTableApi';

// The component that displays the admin subsections table
export default function AdminTableSubsections() {
  const { t } = useTranslation();

  return (
    <AdminTableApi
      tableName={t('admin.subsections')}
      apiPathname="/courseSubsections"
      create={false}
      delete={true}
      update={true}
    />
  );
}
