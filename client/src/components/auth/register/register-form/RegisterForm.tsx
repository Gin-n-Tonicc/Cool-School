import { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { RolesEnum } from '../../../../types/enums/RolesEnum';
import { IUser } from '../../../../types/interfaces/IUser';
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
  const { t } = useTranslation();
  const { auth: validators } = useValidators();

  const navigate = useNavigate();
  const { loginUser } = useAuthContext();
  const { post, response } = useFetch<IUser>(apiUrlsConfig.auth.register);

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
        message: t('auth.repeat.password.not-match'),
      });
    }

    if (hasManualError && areEqual) {
      clearErrors('Repeat your password');
    }
  }, [errors, formValues, setError, clearErrors]);

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await post({
      firstname: data['First Name'].trim(),
      lastname: data['Last Name'].trim(),
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
        placeholder={t('register.first.name')}
        name="First Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={validators.FIRST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.last.name')}
        name="Last Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={validators.LAST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.username')}
        name="Username"
        type="text"
        iconClasses="zmdi zmdi-account material-icons-name"
        rules={validators.USERNAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.description')}
        name="Description"
        type="text"
        iconClasses="zmdi zmdi-book"
        rules={validators.DESCRIPTION_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.address')}
        name="Address"
        type="text"
        iconClasses="zmdi zmdi-pin"
        rules={validators.ADDRESS_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.email')}
        name="Email"
        type="email"
        iconClasses="zmdi zmdi-email"
        rules={validators.EMAIL_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.password')}
        name="Password"
        type="password"
        iconClasses="zmdi zmdi-lock"
        rules={validators.PASSWORD_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('register.repeat.password')}
        name="Repeat your password"
        type="password"
        iconClasses="zmdi zmdi-lock-outline"
        rules={validators.REPEAT_PASSWORD_VALIDATIONS}
      />

      <FormErrorWrapper message={undefined}>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.USER}
          />
          <p>{t('register.role.student')}</p>
        </div>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.TEACHER}
          />
          <p>{t('register.role.teacher')}</p>
        </div>
      </FormErrorWrapper>

      <div className="form-group form-button">
        <input
          type="submit"
          name="signup"
          id="signup"
          className="btn_1"
          value={t('finish.register.complete')}
        />
      </div>
    </form>
  );
}
