import { useTranslation } from 'react-i18next';
import '../styles/common_learning.scss';
import advanceFeatureImg from './images/advance_feature_img.png';

// The component that displays information about our learning features
export default function HomeLearningFeature() {
  const { t } = useTranslation();

  return (
    <section className="advance_feature learning_part">
      <div className="container">
        <div className="row align-items-sm-center align-items-xl-stretch advance_feature_content_wrapper">
          <div className="col-md-5 col-lg-5">
            <div className="learning_member_text">
              <h5>{t('home.personalized.progress')}</h5>
              <h2>{t('home.learning.environment')}</h2>
              <p>{t('home.learning.environment.description')}</p>
              <div className="row">
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-pencil-alt"></span>
                    <h4>{t('home.learn.everywhere')}</h4>
                    <p>{t('home.learn.everywhere.description')}</p>
                  </div>
                </div>
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-stamp"></span>
                    <h4>{t('home.add.something.form.yourself')}</h4>
                    <p>{t('home.add.something.form.yourself.description')}</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-7 col-md-7">
            <div className="learning_img">
              <img src={advanceFeatureImg} alt="" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
