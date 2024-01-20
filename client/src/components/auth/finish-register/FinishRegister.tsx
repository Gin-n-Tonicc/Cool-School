import { useTranslation } from 'react-i18next';
import { Navigate } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import signUpImg from '../register/signup-image.jpg';
import FinishRegisterForm from './finish-register-form/FinishRegisterForm';

export default function FinishRegister() {
  const { t } = useTranslation();
  const { hasFinishedOAuth2 } = useAuthContext();

  if (hasFinishedOAuth2) {
    return <Navigate to={PagesEnum.Home} />;
  }

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">{t('finish.register.text')}</h2>
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
