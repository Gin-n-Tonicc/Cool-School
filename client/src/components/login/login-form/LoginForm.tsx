import { useCallback } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useAuthContext } from '../../../contexts/AuthContext';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  Username: string;
  Password: string;
  rememberMe: boolean;
};

export default function LoginForm() {
  const { user, isAuthenticated, loginUser, logoutUser } = useAuthContext();

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Username: '',
      Password: '',
      rememberMe: false,
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = useCallback(
    (data) => {
      // Debug purposes
      if (!isAuthenticated) {
        loginUser({
          accessToken: 'AccessToken',
          refreshToken: 'refreshToken',
          user: {
            email: 'email',
            firstname: 'first name',
            _id: 1,
            username: 'username',
          },
        });
      } else {
        logoutUser();
      }

      console.log(data);
    },
    [user]
  );

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
