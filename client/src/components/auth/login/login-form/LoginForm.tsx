import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { Link, useNavigate } from 'react-router-dom';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { useFetch } from '../../../../hooks/useFetch';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { IAuthResponse } from '../../../../types/interfaces/auth/IAuthResponse';
import FormInput from '../../../common/form-input/FormInput';

type LoginFormProps = {
  redirectTo: string | null;
};

type Inputs = {
  Email: string;
  Password: string;
  rememberMe: boolean;
};

// The component used to display and handle the login form
export default function LoginForm({ redirectTo }: LoginFormProps) {
  const { t } = useTranslation();
  const { auth: validators } = useValidators();
  const navigate = useNavigate();
  const { loginUser } = useAuthContext();

  // Prepare fetches
  const { post, response } = useFetch<IAuthResponse>(apiUrlsConfig.auth.login);

  // Handle form
  const { handleSubmit, control, reset } = useForm<Inputs>({
    defaultValues: {
      Email: '',
      Password: '',
    },
    mode: 'onChange',
  });

  // Handle form submit
  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await post({
      email: data.Email.trim(),
      password: data.Password.trim(),
    });

    // Reset form, update auth state (login user) and navigate
    if (response.ok) {
      reset();
      loginUser(user);

      if (redirectTo) {
        navigate(redirectTo);
      } else {
        navigate(PagesEnum.Home);
      }
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="register-form"
      id="login-form">
      <FormInput
        control={control}
        placeholder={t('login.email')}
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={validators.EMAIL_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('login.password')}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={validators.PASSWORD_VALIDATIONS}
      />

      <div className="flex flex-column justify-center items-end gap-1">
        <div className="form-group form-button login-form-button">
          <input
            type="submit"
            name="signin"
            id="signin"
            className="btn_1"
            value={t('login.button')}
          />
        </div>
        <Link
          to={`${PagesEnum.ForgottenPassword}${
            redirectTo ? `?redirect=${redirectTo}` : ''
          }`}>
          <h6 className="auth-link">{t('login.forgot.password.text')}?</h6>
        </Link>
      </div>
    </form>
  );
}
