import { useCallback, useEffect, useMemo } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { ICategory } from '../../../types/interfaces/ICategory';
import { IFile } from '../../../types/interfaces/IFile';
import {
  CATEGORY_VALIDATIONS,
  IMAGE_FILE_VALIDATIONS,
} from '../../../validations/commonValidations';

import {
  ELIGIBILITY_VALIDATIONS,
  NAME_VALIDATIONS,
  OBJECTIVES_VALIDATIONS,
} from '../../../validations/courseCreateValidations';
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

export default function CoursesCreate() {
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

  useEffect(() => {
    register('category', { ...CATEGORY_VALIDATIONS });
  }, []);

  const fileLabelText = useMemo(
    () => values.file[0]?.name || 'Choose course image',
    [values.file]
  );

  const { data: categories } = useFetch<ICategory[]>(
    apiUrlsConfig.categories.get,
    []
  );

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

  const onSubmit: SubmitHandler<Inputs> = async (data) => {
    const fileFormData = new FormData();
    fileFormData.append('file', data.file[0]);
    const file = await filePost(fileFormData);

    if (!postFileRes.ok) {
      return;
    }

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
            <h2 className="form-title">Create Course</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                name="Name"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={NAME_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.objectives?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>Course Objectives</h5>
                  <textarea
                    className="form-control"
                    {...register('objectives', { ...OBJECTIVES_VALIDATIONS })}
                    rows={3}></textarea>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.eligibility?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>Course Eligibility</h5>
                  <textarea
                    className="form-control"
                    {...register('eligibility', { ...ELIGIBILITY_VALIDATIONS })}
                    rows={3}></textarea>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.file?.message}>
                <div className="custom-file">
                  <input
                    type="file"
                    className="custom-file-input"
                    {...register('file', { ...IMAGE_FILE_VALIDATIONS })}
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
                  onCategoryChange={onCategoryChange}
                />
              </FormErrorWrapper>

              <div className="form-group form-button">
                <input
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  value="Create Blog"
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
