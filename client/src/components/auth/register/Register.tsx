import { Link } from 'react-router-dom';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import '../../common/scss/authentication.scss';
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
            <Link
              to={`/login${redirectTo ? `?redirect=${redirectTo}` : ''}`}
              className="signup-image-link">
              Already registered?
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
