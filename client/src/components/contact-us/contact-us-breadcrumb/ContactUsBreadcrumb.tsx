import { useTranslation } from 'react-i18next';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';

export default function ContactUsBreadcrumb() {
  const { t } = useTranslation();

  return (
    <Breadcrumb
      heading={t('contact.contact.us.capitalized')}
      pageName="Contact us"
    />
  );
}
