import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { IUser } from '../../../../types/interfaces/IUser';
import FormInput from '../../../common/form-input/FormInput';

type LoginFormProps = {
  redirectTo: string | null;
};

type Inputs = {
  Email: string;
  Password: string;
  rememberMe: boolean;
};

export default function LoginForm({ redirectTo }: LoginFormProps) {
  const { t } = useTranslation();
  const { auth: validators } = useValidators();
  const navigate = useNavigate();

  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IUser>(apiUrlsConfig.auth.login);

  const { handleSubmit, control, reset } = useForm<Inputs>({
    defaultValues: {
      Email: '',
      Password: '',
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await post({
      email: data.Email.trim(),
      password: data.Password.trim(),
    });

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

      <div className="form-group form-button">
        <input
          type="submit"
          name="signin"
          id="signin"
          className="btn_1"
          value={t('login.button')}
        />
      </div>
    </form>
  );
}
