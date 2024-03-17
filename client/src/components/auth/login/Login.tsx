import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import '../../common/scss/authentication.scss';
import googleSvg from '../images/google.svg';
import LoginForm from './login-form/LoginForm';
import signInImage from './signin-image.jpg';

// The component used to display the Login page
export default function Login() {
  const { t } = useTranslation();

  // Extract and pass redirectTo to the login form
  const redirectTo = useUrlSearchParam('redirect');

  return (
    <section className="sign-in">
      <div className="sign-container">
        <div className="signin-content">
          <div className="signin-image">
            <figure>
              <img src={signInImage} alt="sing in image" />
            </figure>

            <section className="external-login">
              <div className="signup-image-link">
                <h6>
                  {t('login.no.profile.yet')}{' '}
                  <Link
                    to={`${PagesEnum.Register}${
                      redirectTo ? `?redirect=${redirectTo}` : ''
                    }`}>
                    <h5>{t('login.register')}</h5>
                  </Link>
                </h6>
              </div>
              <h6 className="or-login-text">{t('login.or.with')}</h6>
              <a
                className="external-login-btn"
                href={apiUrlsConfig.oAuth.google}
                title={t('auth.with.google')}>
                <img
                  src={googleSvg}
                  className="external-login-btn-icon"
                  alt="Use Google"
                />
                Google
              </a>
            </section>
          </div>

          <div className="signin-form">
            <h2 className="form-title">{t('login.login')}</h2>
            <LoginForm redirectTo={redirectTo} />
          </div>
        </div>
      </div>
    </section>
  );
}
