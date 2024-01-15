import signUpImg from '../register/signup-image.jpg';
import FinishRegisterForm from './finish-register-form/FinishRegisterForm';

export default function FinishRegister() {
  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">Finish Register</h2>
            <FinishRegisterForm />
          </div>
          <div className="signup-image">
            <figure>
              <img src={signUpImg} alt="sing up image" />
            </figure>
          </div>
        </div>
      </div>
    </section>
  );
}
