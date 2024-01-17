import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import '../../common/scss/authentication.scss';
import googleSvg from '../images/google.svg';
import RegisterForm from './register-form/RegisterForm';
import signUpImg from './signup-image.jpg';

export default function Register() {
  const redirectTo = useUrlSearchParam('redirect');

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">Register</h2>
            <RegisterForm redirectTo={redirectTo} />
          </div>
          <div className="signup-image">
            <figure>
              <img src={signUpImg} alt="sing up image" />
            </figure>
            <section className="external-login">
              <div className="signup-image-link">
                <h6>
                  Already registered?{' '}
                  <Link
                    to={`${PagesEnum.Login}${
                      redirectTo ? `?redirect=${redirectTo}` : ''
                    }`}>
                    <h5>Login!</h5>
                  </Link>
                </h6>
              </div>
              <h6 className="or-login-text">Or register with</h6>
              <a
                className="external-login-btn"
                href={apiUrlsConfig.oAuth.google}
                title="Log in using your Google account">
                <img
                  src={googleSvg}
                  className="external-login-btn-icon"
                  alt="Използвай Google"
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
