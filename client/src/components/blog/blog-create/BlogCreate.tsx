import { SubmitHandler, useForm } from 'react-hook-form';
import {
  FIRST_NAME_VALIDATIONS,
  LAST_NAME_VALIDATIONS,
  USERNAME_VALIDATIONS,
} from '../../../utils/validationConstants';
import FormInput from '../../common/form-input/FormInput';
import './BlogCreate.scss';

type Inputs = {
  Title: string;
  Summary: string;
  Content: string;
  file: File[];
};

export default function BlogCreate() {
  const {
    handleSubmit,
    control,
    reset,
    watch,
    register,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Title: '',
      Summary: '',
      Content: '',
      file: [],
    },
    mode: 'onChange',
  });

  const values = watch();
  const labelText = values.file[0]?.name || 'Choose file';

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    console.log(data.file);
  };

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form">
            <h2 className="form-title">Create Blog</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                name="Title"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={FIRST_NAME_VALIDATIONS}
              />

              <FormInput
                control={control}
                name="Summary"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={LAST_NAME_VALIDATIONS}
              />

              <FormInput
                control={control}
                name="Content"
                type="text"
                iconClasses="zmdi zmdi-account material-icons-name"
                rules={USERNAME_VALIDATIONS}
              />

              <div className="form-group">
                <div className="custom-file">
                  <input
                    type="file"
                    className="custom-file-input"
                    {...register('file')}
                  />
                  <label className="custom-file-label">{labelText}</label>
                </div>
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
          </div>
        </div>
      </div>
    </section>
  );
}
