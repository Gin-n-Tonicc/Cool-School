import { useCallback, useEffect, useMemo } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { ICategory } from '../../../types/interfaces/common/ICategory';
import { IFile } from '../../../types/interfaces/common/IFile';

import { useTranslation } from 'react-i18next';
import { useFetch } from '../../../hooks/useFetch';
import useValidators from '../../../hooks/useValidator/useValidators';
import CategorySelect from '../../common/category-select/CategorySelect';
import FormErrorWrapper from '../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../common/form-input/FormInput';

type Inputs = {
  Name: string;
  objectives: string;
  eligibility: string;
  category: number;
  file: File[];
};

// The component that displays
// a form for blog creation
export default function CoursesCreate() {
  const { t } = useTranslation();
  const { common, courseCreate } = useValidators();

  // Handle form
  const {
    handleSubmit,
    control,
    reset,
    watch,
    register,
    setValue,
    formState: { errors },
  } = useForm<Inputs>({
    defaultValues: {
      Name: '',
      objectives: '',
      eligibility: '',
      category: -1,
      file: [],
    },
    mode: 'onChange',
  });

  const values = watch();
  const navigate = useNavigate();
  const { user } = useAuthContext();

  // Register the category dropdown
  useEffect(() => {
    register('category', { ...common.CATEGORY_VALIDATIONS });
  }, []);

  // Figure out the name of the file
  const fileLabelText = useMemo(
    () => values.file[0]?.name || t('courses.create.choose.image'),
    [values.file, t]
  );

  // Fetch categories on load
  const { data: categories } = useFetch<ICategory[]>(
    apiUrlsConfig.categories.get,
    []
  );

  // Prepare fetches
  const { post: filePost, response: postFileRes } = useFetch<IFile>(
    apiUrlsConfig.files.upload()
  );

  const { post: coursePost, response: postCourseRes } = useFetch<any>(
    apiUrlsConfig.courses.upload
  );

  const onCategoryChange = useCallback(
    (numberVal: number) => {
      setValue('category', numberVal, {
        shouldValidate: true,
        shouldDirty: true,
        shouldTouch: true,
      });
    },
    [setValue]
  );

  // Handle form submit
  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    // Post the course image first
    const fileFormData = new FormData();
    fileFormData.append('file', data.file[0]);
    const file = await filePost(fileFormData);

    if (!postFileRes.ok) {
      return;
    }

    // If successful image post
    // Create the course with the just created image
    const body = {
      name: data.Name.trim(),
      objectives: data.objectives.trim(),
      eligibility: data.eligibility.trim(),
      stars: 0,
      pictureId: file.id,
      categoryId: data.category,
      userId: user.id,
    };

    const course = await coursePost(body);

    // After which redirect to the course page
    if (postCourseRes.ok) {
      reset();
      navigate(PagesEnum.SingleCourse.replace(':id', course.id.toString()));
    }
  };

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form create-blog-form">
            <h2 className="form-title">{t('courses.create')}</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                placeholder={t('courses.create.name')}
                name="Name"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={courseCreate.NAME_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.objectives?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>{t('courses.create.objectives')}</h5>
                  <textarea
                    className="form-control"
                    {...register('objectives', {
                      ...courseCreate.OBJECTIVES_VALIDATIONS,
                    })}
                    rows={3}></textarea>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.eligibility?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>{t('courses.create.eligibility')}</h5>
                  <textarea
                    className="form-control"
                    {...register('eligibility', {
                      ...courseCreate.ELIGIBILITY_VALIDATIONS,
                    })}
                    rows={3}></textarea>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.file?.message}>
                <div className="custom-file">
                  <input
                    type="file"
                    className="custom-file-input"
                    {...register('file', { ...common.FILE_VALIDATIONS })}
                  />
                  <label className="custom-file-label">{fileLabelText}</label>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.category?.message}>
                <CategorySelect
                  categories={
                    categories?.map((x) => ({
                      value: x.id.toString(),
                      label: x.name,
                    })) || []
                  }
                  placeholder={t('common.category.select.placeholder')}
                  onCategoryChange={onCategoryChange}
                />
              </FormErrorWrapper>

              <div className="form-group form-button">
                <input
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  value={t('courses.create.button')}
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
