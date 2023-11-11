import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { IAuthResponse } from '../../../../types/interfaces/IAuthResponse';
import {
  EMAIL_VALIDATIONS,
  PASSWORD_VALIDATIONS,
} from '../../../../validations/authValidations';
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
  const navigate = useNavigate();

  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IAuthResponse>(apiUrlsConfig.auth.login);

  const { handleSubmit, control, reset } = useForm<Inputs>({
    defaultValues: {
      Email: '',
      Password: '',
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await post({
      email: data.Email,
      password: data.Password,
    });

    if (response.ok) {
      reset();
      loginUser(user);

      if (redirectTo) {
        navigate(redirectTo);
      } else {
        navigate('/');
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
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={EMAIL_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={PASSWORD_VALIDATIONS}
      />

      <div className="form-group form-button">
        <input
          type="submit"
          name="signin"
          id="signin"
          className="btn_1"
          value="Log in"
        />
      </div>
    </form>
  );
}
