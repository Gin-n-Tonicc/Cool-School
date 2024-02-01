import { useCallback, useEffect, useMemo } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import useValidators from '../../../hooks/useValidator/useValidators';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IBlog } from '../../../types/interfaces/IBlog';
import { ICategory } from '../../../types/interfaces/ICategory';
import { IFile } from '../../../types/interfaces/IFile';
import CategorySelect from '../../common/category-select/CategorySelect';
import FormErrorWrapper from '../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../common/form-input/FormInput';
import './BlogCreate.scss';

type Inputs = {
  Title: string;
  Summary: string;
  content: string;
  category: number;
  file: File[];
};

export default function BlogCreate() {
  const { t } = useTranslation();
  const { common, blogCreate } = useValidators();

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
      Title: '',
      Summary: '',
      content: '',
      category: -1,
      file: [],
    },
    mode: 'onChange',
  });

  const values = watch();
  const navigate = useNavigate();

  useEffect(() => {
    register('category', { ...common.CATEGORY_VALIDATIONS });
  }, []);

  const { data: categories } = useFetch<ICategory[]>(
    apiUrlsConfig.categories.get,
    []
  );

  const { post: filePost, response: postFileRes } = useFetch<IFile>(
    apiUrlsConfig.files.upload()
  );

  const { post: blogPost, response: postBlogRes } = useFetch<IBlog>(
    apiUrlsConfig.blogs.upload
  );

  const labelText = useMemo(
    () => values.file[0]?.name || t('blogs.create.choose.image'),
    [values.file, t]
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
      title: data.Title.trim(),
      content: data.content.trim(),
      summary: data.Summary.trim(),
      liked_users: [],
      pictureId: file.id,
      categoryId: data.category,
    };

    const blog = await blogPost(body);
    if (postBlogRes.ok) {
      reset();
      navigate(PagesEnum.SingleBlog.replace(':id', blog.id.toString()));
    }
  };

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form create-blog-form">
            <h2 className="form-title">{t('blogs.create')}</h2>
            <form
              onSubmit={handleSubmit(onSubmit)}
              className="register-form"
              id="register-form">
              <FormInput
                control={control}
                placeholder={t('blogs.create.title')}
                name="Title"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={blogCreate.TITLE_VALIDATIONS}
              />

              <FormInput
                control={control}
                placeholder={t('blogs.create.summary')}
                name="Summary"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={blogCreate.SUMMARY_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.content?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>{t('blogs.create.content')}</h5>
                  <textarea
                    className="form-control"
                    {...register('content', {
                      ...blogCreate.CONTENT_VALIDATIONS,
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
                  <label className="custom-file-label">{labelText}</label>
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
                  value={t('blogs.create.button')}
                />
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
}
