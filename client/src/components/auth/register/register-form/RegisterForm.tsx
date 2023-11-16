import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { RolesEnum } from '../../../../types/enums/RolesEnum';
import { IAuthResponse } from '../../../../types/interfaces/IAuthResponse';
import {
  ADDRESS_VALIDATIONS,
  DESCRIPTION_VALIDATIONS,
  EMAIL_VALIDATIONS,
  FIRST_NAME_VALIDATIONS,
  LAST_NAME_VALIDATIONS,
  PASSWORD_VALIDATIONS,
  REPEAT_PASSWORD_VALIDATIONS,
  USERNAME_VALIDATIONS,
} from '../../../../validations/authValidations';
import FormErrorWrapper from '../../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../../common/form-input/FormInput';

type RegisterFormProps = {
  redirectTo: string | null;
};

type Inputs = {
  'First Name': string;
  'Last Name': string;
  Username: string;
  Address: string;
  Email: string;
  Password: string;
  'Repeat your password': string;
  Description: string;
  role: RolesEnum;
};

export default function RegisterForm({ redirectTo }: RegisterFormProps) {
  const navigate = useNavigate();
  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IAuthResponse>(
    apiUrlsConfig.auth.register
  );

  const {
    handleSubmit,
    control,
    register,
    reset,
    watch,
    setError,
    clearErrors,
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
      Description: '',
      role: RolesEnum.USER,
    },
    mode: 'onChange',
  });

  const formValues = watch();

  useEffect(() => {
    const areEqual = formValues.Password === formValues['Repeat your password'];
    const hasError = Boolean(errors['Repeat your password']);
    const hasManualError =
      hasError && errors['Repeat your password']?.type === 'manual';

    if (!hasError && !areEqual) {
      setError('Repeat your password', {
        type: 'manual',
        message: 'Repeat password must match password.',
      });
    }

    if (hasManualError && areEqual) {
      clearErrors('Repeat your password');
    }
  }, [errors, formValues, setError, clearErrors]);

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await post({
      firstname: data['First Name'],
      lastname: data['Last Name'],
      email: data.Email.trim(),
      password: data.Password.trim(),
      address: data.Address.trim(),
      username: data.Username.trim(),
      description: data.Description.trim(),
      role: data['role'].trim(),
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
      id="register-form">
      <FormInput
        control={control}
        name="First Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={FIRST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Last Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={LAST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Username"
        type="text"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={USERNAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Description"
        type="text"
        iconClasses="zmdi zmdi-book"
        rules={DESCRIPTION_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Address"
        type="text"
        iconClasses="zmdi zmdi-pin"
        rules={ADDRESS_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-email"
        rules={EMAIL_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={PASSWORD_VALIDATIONS}
      />

      <FormInput
        control={control}
        name="Repeat your password"
        type="password"
        iconClasses="zmdi zmdi-lock-outline"
        rules={REPEAT_PASSWORD_VALIDATIONS}
      />

      <FormErrorWrapper message={undefined}>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.USER}
          />
          <p>Student</p>
        </div>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.TEACHER}
          />
          <p>Teacher</p>
        </div>
      </FormErrorWrapper>

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
