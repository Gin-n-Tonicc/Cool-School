import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import '../../common/scss/authentication.scss';
import googleSvg from '../images/google.svg';
import RegisterForm from './register-form/RegisterForm';
import signUpImg from './signup-image.jpg';

// The component used to display the Register page
export default function Register() {
  const { t } = useTranslation();

  // Keep the redirectTo searchParam (might get passed to login)
  const redirectTo = useUrlSearchParam('redirect');

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">{t('register.register')}</h2>
            <RegisterForm />
          </div>
          <div className="signup-image">
            <figure>
              <img src={signUpImg} alt="sing up image" />
            </figure>
            <section className="external-login">
              <div className="signup-image-link">
                <h6>
                  {t('register.already.registered')}{' '}
                  <Link
                    to={`${PagesEnum.Login}${
                      redirectTo ? `?redirect=${redirectTo}` : ''
                    }`}>
                    <h5>{t('register.login')}</h5>
                  </Link>
                </h6>
              </div>
              <h6 className="or-login-text">{t('register.or.with')}</h6>
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
        </div>
      </div>
    </section>
  );
}
