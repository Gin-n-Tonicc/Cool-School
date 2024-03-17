import { useTranslation } from 'react-i18next';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';

// Breadcrumb for the Courses page
export default function CoursesBreadcrumb() {
  const { t } = useTranslation();

  return <Breadcrumb heading={t('courses.courses')} pageName="Courses" />;
}
