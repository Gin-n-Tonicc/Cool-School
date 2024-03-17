import { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { initialAuthUtils } from '../../../utils/initialAuthUtils';
import signUpImg from '../register/signup-image.jpg';
import FinishRegisterForm from './finish-register-form/FinishRegisterForm';

// The component used to display the finish register page
export default function FinishRegister() {
  const { t } = useTranslation();
  const { hasFinishedOAuth2, user } = useAuthContext();
  const navigate = useNavigate();

  // Allow only not fully registered oauth2 users to finish their registration
  useEffect(() => {
    if (initialAuthUtils.hasFinishedInitialAuth() && hasFinishedOAuth2) {
      navigate(PagesEnum.Home);
    }
  }, [user]);

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
