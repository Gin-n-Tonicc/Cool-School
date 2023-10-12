import { Link } from 'react-router-dom';
import '../../common/scss/authentication.scss';
import LoginForm from './login-form/LoginForm';
import signInImage from './signin-image.jpg';

export default function Login() {
  return (
    <section className="sign-in">
      <div className="sign-container">
        <div className="signin-content">
          <div className="signin-image">
            <figure>
              <img src={signInImage} alt="sing in image" />
            </figure>
            <Link to="/register" className="signup-image-link">
              Create an account?
            </Link>
          </div>

          <div className="signin-form">
            <h2 className="form-title">Login</h2>
            <LoginForm />
          </div>
        </div>
      </div>
    </section>
  );
}
