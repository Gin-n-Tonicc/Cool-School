import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import '../styles/common_learning.scss';
import learningImg from './images/learning_img.png';

// The component that displays information about our learning features
export default function HomeLearning() {
  const { t } = useTranslation();

  return (
    <section className="learning_part">
      <div className="container">
        <div className="row align-items-sm-center align-items-lg-stretch">
          <div className="col-md-7 col-lg-7">
            <div className="learning_img">
              <img src={learningImg} alt="" />
            </div>
          </div>
          <div className="col-md-5 col-lg-5">
            <div className="learning_member_text">
              <h5>{t('home.our.site')}</h5>
              <h2>{t('home.engage.and.connect')}</h2>
              <p>{t('home.engage.and.connect.description')}</p>
              <ul>
                <li>
                  <span className="ti-pencil-alt"></span>
                  {t('home.engage.and.connect.description.first.text')}
                </li>
                <li>
                  <span className="ti-ruler-pencil"></span>
                  {t('home.engage.and.connect.description.second.text')}
                </li>
              </ul>
              <Link to={PagesEnum.Blog} className="btn_1">
                {t('home.read.more.button')}
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
