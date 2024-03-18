import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useErrorContext } from '../../../contexts/ErrorContext';
import { useFetch } from '../../../hooks/useFetch';
import useUrlSearchParam from '../../../hooks/useURLSearchParam';
import useValidators from '../../../hooks/useValidator/useValidators';
import { ErrorTypeEnum } from '../../../types/enums/ErrorTypeEnum';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  Email: string;
  Password: string;
  NewPassword: string;
};

// The component used to display the forgotten password page
export default function ForgottenPassword() {
  const { t } = useTranslation();
  const { auth } = useValidators();
  const { addError } = useErrorContext();

  // Grab token from search params
  const token = useUrlSearchParam('token') || '';

  // Handle form
  const { handleSubmit, control, reset, watch } = useForm<Inputs>({
    defaultValues: {
      Email: '',
      Password: '',
      NewPassword: '',
    },
    mode: 'onChange',
  });

  const values = watch();
  const navigate = useNavigate();

  // Prepare fetches
  const {
    post: forgotPasswordPost,
    response: forgotPasswordRes,
    loading: forgotPasswordLoading,
  } = useFetch<string>(apiUrlsConfig.auth.forgotPassword(values.Email));

  const {
    post: resetPasswordPost,
    response: resetPasswordRes,
    loading: resetPasswordLoading,
  } = useFetch<string>(
    apiUrlsConfig.auth.resetPassword(token, values.NewPassword)
  );

  const hasToken = Boolean(token);
  const loading = forgotPasswordLoading || resetPasswordLoading;

  // Handle form submit
  const onSubmit: SubmitHandler<Inputs> = async () => {
    if (!hasToken) {
      onEmailSend();
    } else {
      onPasswordChange();
    }
  };

  // Send reset password email
  const onEmailSend = async () => {
    await forgotPasswordPost();

    if (forgotPasswordRes.ok) {
      reset();
      addError(
        t('forgotten.password.successful.email'),
        ErrorTypeEnum.HEADS_UP
      );
    }
  };

  // Reset password
  const onPasswordChange = async () => {
    await resetPasswordPost();

    if (resetPasswordRes.ok) {
      reset();
      addError(
        t('forgotten.password.successful.reset'),
        ErrorTypeEnum.HEADS_UP
      );
      navigate(PagesEnum.Login);
    }
  };

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form create-blog-form">
            <h2 className="form-title">{t('forgotten.password.title')}</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              {!hasToken && (
                <FormInput
                  control={control}
                  placeholder={t('forgotten.password.email')}
                  name="Email"
                  type="text"
                  iconClasses="zmdi zmdi-face material-icons-name"
                  rules={auth.EMAIL_VALIDATIONS}
                />
              )}

              {hasToken && (
                <FormInput
                  control={control}
                  placeholder={t('forgotten.password.new.password')}
                  name="NewPassword"
                  type="password"
                  iconClasses="zmdi zmdi-lock"
                  rules={auth.PASSWORD_VALIDATIONS}
                />
              )}

              <div className="form-group form-button">
                <input
                  disabled={loading}
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  value={
                    hasToken
                      ? t('forgotten.password.button.reset.password')
                      : t('forgotten.password.button.send.email')
                  }
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
