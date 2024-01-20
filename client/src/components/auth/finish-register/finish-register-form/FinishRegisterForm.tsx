import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { RolesEnum } from '../../../../types/enums/RolesEnum';
import { IUser } from '../../../../types/interfaces/IUser';
import FormErrorWrapper from '../../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../../common/form-input/FormInput';

type Inputs = {
  'First Name': string;
  'Last Name': string;
  Address: string;
  Description: string;
  role: RolesEnum;
};

export default function FinishRegisterForm() {
  const { t } = useTranslation();
  const validators = useValidators();
  const navigate = useNavigate();

  const { loginUser } = useAuthContext();
  const { put, response } = useFetch<IUser>(apiUrlsConfig.auth.completeOAuth);

  const { handleSubmit, register, control, reset } = useForm<Inputs>({
    defaultValues: {
      'First Name': '',
      'Last Name': '',
      Address: '',
      Description: '',
      role: RolesEnum.USER,
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const user = await put({
      firstname: data['First Name'].trim(),
      lastname: data['Last Name'].trim(),
      address: data.Address.trim(),
      description: data.Description.trim(),
      role: data['role'].trim(),
    });

    if (response.ok) {
      reset();
      loginUser(user);
      navigate(PagesEnum.Home);
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className="register-form"
      id="register-form">
      <FormInput
        control={control}
        placeholder={t('finish.register.first.name')}
        name="First Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={validators.FIRST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('finish.register.last.name')}
        name="Last Name"
        type="text"
        iconClasses="zmdi zmdi-face material-icons-name"
        rules={validators.LAST_NAME_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('finish.register.description')}
        name="Description"
        type="text"
        iconClasses="zmdi zmdi-book"
        rules={validators.DESCRIPTION_VALIDATIONS}
      />

      <FormInput
        control={control}
        placeholder={t('finish.register.address')}
        name="Address"
        type="text"
        iconClasses="zmdi zmdi-pin"
        rules={validators.ADDRESS_VALIDATIONS}
      />

      <FormErrorWrapper message={undefined}>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.USER}
          />
          <p>{t('finish.register.role.student')}</p>
        </div>
        <div className="form-check-inline">
          <input
            {...register('role')}
            type="radio"
            className="form-check-input"
            value={RolesEnum.TEACHER}
          />
          <p>{t('finish.register.role.teacher')}</p>
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
