import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import './HomeFeatures.scss';
import HomeFeature from './home-feature/HomeFeature';

// The component that displays information about our learning features
export default function HomeFeatures() {
  const { t } = useTranslation();

  return (
    <section className="feature_part">
      <div className="container">
        <div className="row">
          <div className="col-sm-6 col-xl-3 align-self-center">
            <div className="single_feature_text">
              <h2>
                {t('home.what.makes.it.cool')}
                <br />
              </h2>
              <p>{t('home.what.makes.it.cool.description')}</p>
              <Link to={PagesEnum.Courses} className="btn_1">
                {t('home.read.more.button')}
              </Link>
            </div>
          </div>
          <HomeFeature
            iconClassName="ti-layers"
            featureTitle={t('home.flexibility')}
            featureDescription={t('home.flexibility.description')}
          />

          <HomeFeature
            iconClassName="ti-new-window"
            featureTitle={t('home.personalized.pathways')}
            featureDescription={t('home.personalized.pathways.description')}
          />

          <HomeFeature
            iconClassName="ti-light-bulb"
            featureTitle={t('home.anywhere.learning.hub')}
            featureDescription={t('home.anywhere.learning.hub.description')}
          />
        </div>
      </div>
    </section>
  );
}
