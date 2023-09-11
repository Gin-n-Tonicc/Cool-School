import { useCallback } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { CachePolicies, useFetch } from 'use-http';
import { useAuthContext } from '../../../contexts/AuthContext';
import { IAuthResponse } from '../../../interfaces/IAuthResponse';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  Username: string;
  Password: string;
  rememberMe: boolean;
};

export default function LoginForm() {
  const navigate = useNavigate();
  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IAuthResponse>(
    'http://localhost:8080/api/v1/auth/authenticate',
    { cachePolicy: CachePolicies.NO_CACHE }
  );

  const {
    register,
    handleSubmit,
    control,
    reset,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Username: '',
      Password: '',
      rememberMe: false,
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = useCallback(async (data) => {
    const user = await post({
      email: data.Username,
      password: data.Password,
    });

    if (response.ok) {
      reset();
      loginUser(user);
      navigate('/');
    }
  }, []);

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="register-form"
      id="login-form">
      <FormInput
        control={control}
        name="Username"
        type="text"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={{ required: 'Username is required.' }}
      />

      <FormInput
        control={control}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={{ required: 'Password is required.' }}
      />

      <div className="form-group">
        <input
          type="checkbox"
          id="remember-me"
          className="agree-term"
          {...register('rememberMe')}
        />
        <label htmlFor="remember-me" className="label-agree-term">
          <span>
            <span></span>
          </span>
          Remember me
        </label>
      </div>
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
