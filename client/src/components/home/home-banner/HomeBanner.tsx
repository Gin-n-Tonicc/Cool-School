import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import './HomeBanner.scss';

// The component that displays our home banner and
// changes button url based on if the user is authenticated or not
export default function HomeBanner() {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuthContext();

  let url = PagesEnum.Blog;
  if (!isAuthenticated) {
    url = PagesEnum.Login;
  }

  return (
    <section className="banner_part">
      <div className="container">
        <div className="row align-items-center">
          <div className="col-lg-6 col-xl-6">
            <div className="banner_text">
              <div className="banner_text_iner">
                <h5>{t('cool.school').toUpperCase()}</h5>
                <h1>{t('moto')}</h1>
                <p>{t('home.app.description')}</p>
                <Link to={PagesEnum.Courses} className="btn_1">
                  {t('home.view.courses.button')}{' '}
                </Link>
                <Link to={url} className="btn_2">
                  {t('home.get.started.button')}{' '}
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
