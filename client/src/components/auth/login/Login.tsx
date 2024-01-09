import { Link } from 'react-router-dom';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import '../../common/scss/authentication.scss';
import LoginForm from './login-form/LoginForm';
import signInImage from './signin-image.jpg';

export default function Login() {
  const redirectTo = useUrlSearchParam('redirect');

  return (
    <section className="sign-in">
      <div className="sign-container">
        <div className="signin-content">
          <div className="signin-image">
            <figure>
              <img src={signInImage} alt="sing in image" />
            </figure>
            <Link
              to={`/register${redirectTo ? `?redirect=${redirectTo}` : ''}`}
              className="signup-image-link">
              Create an account?
            </Link>
          </div>

          <div className="signin-form">
            <div>
              <h4>
                <a href="http://localhost:8080/oauth2/authorization/google">
                  Login with Google
                </a>
              </h4>
              <h4>
                <a href="http://localhost:8080/oauth2/authorization/github">
                  Login with Github
                </a>
              </h4>
            </div>
            <h2 className="form-title">Login</h2>
            <LoginForm redirectTo={redirectTo} />
          </div>
        </div>
      </div>
    </section>
  );
}
