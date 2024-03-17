import { useTranslation } from 'react-i18next';
import './InternalError.scss';

// The component that displays the internal error page
export default function InternalError() {
  const { t } = useTranslation();

  return (
    <section className="error-container">
      <p className="error-number">500</p>
      <p className="error-title">{t('internal.error.title')}</p>
      <p className="error-text">{t('internal.error.text')}</p>
    </section>
  );
}
