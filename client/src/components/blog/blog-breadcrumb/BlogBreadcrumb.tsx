import { useTranslation } from 'react-i18next';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';

export default function BlogBreadcrumb() {
  const { t } = useTranslation();
  return <Breadcrumb heading={t('blogs.our.blog')} pageName="Blog" />;
}
