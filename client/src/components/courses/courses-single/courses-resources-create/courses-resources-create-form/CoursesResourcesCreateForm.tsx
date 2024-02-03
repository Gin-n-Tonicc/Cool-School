import { SubmitHandler, useForm } from 'react-hook-form';

import FormInput from '../../../../common/form-input/FormInput';

import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../../config/apiUrls';
import { useFetch } from '../../../../../hooks/useFetch';
import useValidators from '../../../../../hooks/useValidator/useValidators';
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
  const { t } = useTranslation();
  const { courseSubsection: validators } = useValidators();

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
            <h5 className="form-title">{t('courses.resources.add')}</h5>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                name="Name"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={validators.TITLE_VALIDATIONS}
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
                  value={t('courses.subsection.submit.button')}
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
