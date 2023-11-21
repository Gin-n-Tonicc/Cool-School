import { SubmitHandler, useForm } from 'react-hook-form';

import { TITLE_VALIDATIONS } from '../../../../../validations/courseSubsectionValidations';
import FormInput from '../../../../common/form-input/FormInput';

import { useMemo } from 'react';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../../config/apiUrls';
import { IFile } from '../../../../../types/interfaces/IFile';
import { IResource } from '../../../../../types/interfaces/IResource';
import FormErrorWrapper from '../../../../common/form-error-wrapper/FormErrorWrapper';

interface CoursesSubsectionCreateFormProps {
  subsectionId: number;
  refreshResources: Function;
}

type Inputs = {
  Name: string;
  file: File[];
};

export default function CoursesResourcesCreateForm(
  props: CoursesSubsectionCreateFormProps
) {
  const {
    handleSubmit,
    watch,
    control,
    reset,
    register,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Name: '',
      file: [],
    },
    mode: 'onChange',
  });

  const values = watch();

  const fileLabelText = useMemo(
    () => values.file[0]?.name || 'Choose resource file',
    [values.file]
  );

  const { post, response } = useFetch<IResource>(
    apiUrlsConfig.resources.create
  );

  const { post: filePost, response: postFileRes } = useFetch<IFile>(
    apiUrlsConfig.files.upload()
  );

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const fileFormData = new FormData();
    fileFormData.append('file', data.file[0]);
    const file = await filePost(fileFormData);

    if (!postFileRes.ok) {
      return;
    }

    const body = {
      name: data.Name.trim(),
      fileId: file.id,
      subsectionId: props.subsectionId,
    };

    await post(body);

    if (response.ok) {
      props.refreshResources();
      reset();
    }
  };

  return (
    <section className="signup sub-create">
      <div className="sign-container sub-create-container">
        <div className="signup-content sub-create-content">
          <div className="signup-form create-blog-form">
            <h5 className="form-title">Add Resource</h5>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                name="Name"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={TITLE_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.file?.message}>
                <div className="custom-file">
                  <input
                    type="file"
                    className="custom-file-input"
                    {...register('file')}
                  />
                  <label className="custom-file-label">{fileLabelText}</label>
                </div>
              </FormErrorWrapper>

              <div className="form-group form-button">
                <input
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  value="Submit"
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
