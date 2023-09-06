import { Link } from 'react-router-dom';
import '../common/scss/authentication.scss';
import signUpImg from './signup-image.jpg';

export default function Register() {
  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">Register</h2>
            <form className="register-form" id="register-form">
              <div className="form-group">
                <label htmlFor="first-name">
                  <i className="zmdi zmdi-face material-icons-name"></i>
                </label>
                <input
                  type="text"
                  name="first-name"
                  id="first-name"
                  placeholder="First Name"
                />
              </div>
              <div className="form-group">
                <label htmlFor="last-name">
                  <i className="zmdi zmdi-face material-icons-name"></i>
                </label>
                <input
                  type="text"
                  name="last-name"
                  id="last-name"
                  placeholder="Last Name"
                />
              </div>
              <div className="form-group">
                <label htmlFor="username">
                  <i className="zmdi zmdi-account material-icons-name"></i>
                </label>
                <input
                  type="text"
                  name="username"
                  id="username"
                  placeholder="Username"
                />
              </div>
              <div className="form-group">
                <label htmlFor="address">
                  <i className="zmdi zmdi-pin"></i>
                </label>
                <input
                  type="text"
                  name="address"
                  id="address"
                  placeholder="Your Address"
                />
              </div>
              <div className="form-group">
                <label htmlFor="email">
                  <i className="zmdi zmdi-email"></i>
                </label>
                <input
                  type="email"
                  name="email"
                  id="email"
                  placeholder="Your Email"
                />
              </div>
              <div className="form-group">
                <label htmlFor="pass">
                  <i className="zmdi zmdi-lock"></i>
                </label>
                <input
                  type="password"
                  name="pass"
                  id="pass"
                  placeholder="Password"
                />
              </div>
              <div className="form-group">
                <label htmlFor="re-pass">
                  <i className="zmdi zmdi-lock-outline"></i>
                </label>
                <input
                  type="password"
                  name="re_pass"
                  id="re_pass"
                  placeholder="Repeat your password"
                />
              </div>
              <div className="form-group">
                <input
                  type="checkbox"
                  name="agree-term"
                  id="agree-term"
                  className="agree-term"
                />
                <label htmlFor="agree-term" className="label-agree-term">
                  <span>
                    <span></span>
                  </span>
                  {/* TODO: Add terms of service link */}I agree all statements
                  in Terms of service
                </label>
              </div>
              <div className="form-group form-button">
                <input
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  value="Register"
                />
              </div>
            </form>
          </div>
          <div className="signup-image">
            <figure>
              <img src={signUpImg} alt="sing up image" />
            </figure>
            <Link to="/login" className="signup-image-link">
              Already registered?
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
}
