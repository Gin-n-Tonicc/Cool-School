import { useTranslation } from 'react-i18next';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';

// The component that displays a breadcrumb for the contact us page
export default function ContactUsBreadcrumb() {
  const { t } = useTranslation();

  return (
    <Breadcrumb
      heading={t('contact.contact.us.capitalized')}
      pageName="Contact us"
    />
  );
}
