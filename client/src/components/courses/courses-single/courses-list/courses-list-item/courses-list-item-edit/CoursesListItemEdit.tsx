import { SubmitHandler, useForm } from 'react-hook-form';

import FormInput from '../../../../../common/form-input/FormInput';

import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../../../config/apiUrls';
import { useFetch } from '../../../../../../hooks/useFetch';
import useValidators from '../../../../../../hooks/useValidator/useValidators';
import { ICourseSubsection } from '../../../../../../types/interfaces/courses/ICourseSubsection';
import FormErrorWrapper from '../../../../../common/form-error-wrapper/FormErrorWrapper';

interface CourseListItemEditProps {
  subsection: ICourseSubsection;
  refreshSubsections: Function;
  courseId: number;
}

type Inputs = {
  Title: string;
  description: string;
};

// The component that displays and handles
// a form for subsection editing
export default function CoursesListItemEdit({
  subsection,
  refreshSubsections,
  courseId,
}: CourseListItemEditProps) {
  const { courseSubsection: validators } = useValidators();
  const { t } = useTranslation();

  // Handle form
  const {
    handleSubmit,
    control,
    reset,
    register,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Title: subsection.title,
      description: subsection.description,
    },
    mode: 'onChange',
  });

  // Prepare fetch
  const { put, response } = useFetch<ICourseSubsection>(
    apiUrlsConfig.courseSubsections.put(subsection.id)
  );

  // Handle form submit
  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const { resources, ...restSubsection } = subsection;

    const body = {
      ...restSubsection,
      title: data.Title.trim(),
      description: data.description.trim(),
      courseId,
    };

    await put(body);

    // After successful response
    // update subsections and set form values to the updated ones
    if (response.ok) {
      await refreshSubsections();
      reset({ Title: body.title, description: body.description });
    }
  };

  return (
    <section className="signup sub-create">
      <div className="sign-container sub-create-container">
        <div className="signup-content sub-create-content">
          <div className="signup-form create-blog-form">
            <h2 className="form-title">{t('courses.subsection.edit')}</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                placeholder={t('courses.subsection.title')}
                name="Title"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={validators.TITLE_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.description?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>{t('courses.subsection.description')}</h5>
                  <textarea
                    className="form-control"
                    {...register('description', {
                      ...validators.DESCRIPTION_VALIDATIONS,
                    })}
                    rows={3}></textarea>
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
