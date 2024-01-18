import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { RolesEnum } from '../../../../types/enums/RolesEnum';
import { IUser } from '../../../../types/interfaces/IUser';
import {
  ADDRESS_VALIDATIONS,
  DESCRIPTION_VALIDATIONS,
  FIRST_NAME_VALIDATIONS,
  LAST_NAME_VALIDATIONS,
} from '../../../../validations/authValidations';
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
