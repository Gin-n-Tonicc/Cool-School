import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { CachePolicies, useFetch } from 'use-http';
import { useAuthContext } from '../../../contexts/AuthContext';
import { IAuthResponse } from '../../../interfaces/IAuthResponse';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  'First Name': string;
  'Last Name': string;
  Username: string;
  Address: string;
  Email: string;
  Password: string;
  'Repeat your password': string;
  agreeTOC: boolean;
};

export default function RegisterForm() {
  const navigate = useNavigate();
  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IAuthResponse>(
    `${process.env.REACT_APP_API_URL}/auth/register`,
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
      'First Name': '',
      'Last Name': '',
      Username: '',
      Address: '',
      Email: '',
      Password: '',
      'Repeat your password': '',
      agreeTOC: false,
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    if (!data.agreeTOC || data.Password !== data['Repeat your password']) {
      // TODO: Add form validations
      return;
    }

    const user = await post({
      firstname: data['First Name'],
      lastname: data['Last Name'],
      email: data.Email,
      password: data.Password,
      address: data.Address,
      username: data.Username,
    });

    if (response.ok) {
      reset();
      loginUser(user);
      navigate('/');
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="register-form"
      id="register-form">
      <FormInput
        control={control}
        name="First Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={{ required: 'First name is required.' }}
      />

      <FormInput
        control={control}
        name="Last Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={{ required: 'Last name is required.' }}
      />

      <FormInput
        control={control}
        name="Username"
        type="text"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={{ required: 'Username is required.' }}
      />

      <FormInput
        control={control}
        name="Address"
        type="text"
        iconClasses="zmdi zmdi-pin"
        rules={{ required: 'Address is required.' }}
      />

      <FormInput
        control={control}
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-email"
        rules={{ required: 'Email is required.' }}
      />

      <FormInput
        control={control}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={{ required: 'Password is required.' }}
      />

      <FormInput
        control={control}
        name="Repeat your password"
        type="password"
        iconClasses="zmdi zmdi-lock-outline"
        rules={{ required: 'Repeat password is required.' }}
      />

      <div className="form-group">
        <input
          type="checkbox"
          id="agree-term"
          className="agree-term"
          {...register('agreeTOC')}
        />
        <label htmlFor="agree-term" className="label-agree-term">
          <span>
            <span></span>
          </span>
          {/* TODO: Add terms of service link */}I agree all statements in Terms
          of service
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
  );
}
