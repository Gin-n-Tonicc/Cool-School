import { useTranslation } from 'react-i18next';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';

export default function CoursesBreadcrumb() {
  const { t } = useTranslation();

  return <Breadcrumb heading={t('courses.courses')} pageName="Courses" />;
}
