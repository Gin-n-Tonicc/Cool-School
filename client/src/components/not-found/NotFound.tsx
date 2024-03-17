import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { PagesEnum } from '../../types/enums/PagesEnum';
import './NotFound.scss';

// The component that displays the notfound page which links to home
export default function NotFound() {
  const { t } = useTranslation();

  return (
    <div id="notfound">
      <div className="notfound">
        <div className="notfound-404">
          <h1>{t('not.found.title')}</h1>
          <h2>404 - {t('not.found.subtitle')}</h2>
        </div>
        <Link to={PagesEnum.Home}>{t('not.found.text')}</Link>
      </div>
    </div>
  );
}
