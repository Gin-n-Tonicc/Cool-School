import { useCallback, useEffect, useMemo, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { SingleValue } from 'react-select';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import useValidators from '../../../hooks/useValidator/useValidators';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IBlog } from '../../../types/interfaces/blogs/IBlog';
import { ICategory } from '../../../types/interfaces/common/ICategory';
import { IFile } from '../../../types/interfaces/common/IFile';
import CategorySelect, {
  CategoryOption,
} from '../../common/category-select/CategorySelect';
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

// The component that displays and handles the form for blog creation
export default function BlogCreate() {
  const { t } = useTranslation();
  const { common, blogCreate } = useValidators();

  const [selectedCategory, setSelectedCategory] = useState<
    SingleValue<CategoryOption> | undefined
  >();

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

  // Register the category dropdown
  useEffect(() => {
    register('category', { ...common.CATEGORY_VALIDATIONS });
  }, []);

  // Prepare fetches
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

  const {
    post: generateAITextPost,
    loading: aiTextLoading,
    response: aiTextRes,
  } = useFetch<string>(apiUrlsConfig.blogs.generateAIContent);

  const {
    post: recommendAICategory,
    loading: aiCategoryLoading,
    response: aiCategoryRes,
  } = useFetch<ICategory>(apiUrlsConfig.blogs.recommendAICategory);

  // Figure out the choose image label text
  const labelText = useMemo(
    () => values.file[0]?.name || t('blogs.create.choose.image'),
    [values.file, t]
  );

  // Clear previous category and then set the new one
  const onCategoryChange = useCallback(
    (numberVal: number) => {
      setSelectedCategory(undefined);

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
    // Post the blog image first
    const fileFormData = new FormData();
    fileFormData.append('file', data.file[0]);
    const file = await filePost(fileFormData);

    if (!postFileRes.ok) {
      return;
    }

    // If successful image post
    // Create the blog with the just created image
    const body = {
      title: data.Title.trim(),
      content: data.content.trim(),
      summary: data.Summary.trim(),
      liked_users: [],
      pictureId: file.id,
      categoryId: data.category,
    };

    // After which redirect to the blog page
    const blog = await blogPost(body);
    if (postBlogRes.ok) {
      reset();
      navigate(PagesEnum.SingleBlog.replace(':id', blog.id.toString()));
    }
  };

  const loadingAI = aiTextLoading || aiCategoryLoading;

  // Ask AI for description based on the blog content
  const onAskAIForDescription = async () => {
    if (loadingAI) {
      return;
    }

    const body = {
      content: values.content,
    };

    const newContent = await generateAITextPost(body);

    if (aiTextRes.ok) {
      setValue('content', newContent, {
        shouldValidate: true,
      });
    }
  };

  // Recommend category with AI based on the blog content
  const onRecommendAICategory = async () => {
    if (loadingAI) {
      return;
    }

    const body = {
      blogContent: values.content,
    };

    const category = await recommendAICategory(body);

    if (aiCategoryRes.ok) {
      onCategoryChange(category.id);

      setSelectedCategory({
        value: category.id.toString(),
        label: category.name,
      });
    }
  };

  const canAskAI = values.content.trim().length >= 10;

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
                <div className="relative">
                  <div className="blog-create-textarea-wrapper">
                    <div className="mb-2">
                      <h4>{t('blogs.create.content')}</h4>
                    </div>
                    <textarea
                      className="form-control"
                      {...register('content', {
                        ...blogCreate.CONTENT_VALIDATIONS,
                      })}
                      readOnly={loadingAI}
                      rows={3}></textarea>
                  </div>

                  {/* Improve description with AI button */}
                  <button
                    type="button"
                    className="improve-with-ai-btn absolute right-0 text-2xl text-center flex flex-column items-center justify-items-center"
                    style={
                      canAskAI && !loadingAI
                        ? {}
                        : {
                            opacity: 0.7,
                            cursor: 'not-allowed',
                          }
                    }
                    disabled={!canAskAI}>
                    <i
                      className="zmdi zmdi-brush rounded-lg"
                      onClick={onAskAIForDescription}></i>
                    <div className="custom-tooltip absolute bg-gray-500 text-white text-sm px-3 py-1 rounded-lg">
                      {t('blogs.create.improve.with.ai')}
                    </div>
                  </button>
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
                <div className="relative my-10 mb-0">
                  <CategorySelect
                    value={selectedCategory}
                    categories={
                      categories?.map((x) => ({
                        value: x.id.toString(),
                        label: x.name,
                      })) || []
                    }
                    placeholder={t('common.category.select.placeholder')}
                    onCategoryChange={onCategoryChange}
                  />

                  {/* Select category with AI button */}
                  <button
                    type="button"
                    className="improve-with-ai-btn improve-with-ai-btn--category absolute right-0 text-2xl text-center flex flex-column items-center justify-items-center"
                    style={
                      canAskAI && !loadingAI
                        ? {}
                        : {
                            opacity: 0.7,
                            cursor: 'not-allowed',
                          }
                    }
                    disabled={!canAskAI}>
                    <i
                      className="zmdi zmdi-brush rounded-lg"
                      onClick={onRecommendAICategory}></i>
                    <div className="custom-tooltip absolute bg-gray-500 text-white text-sm px-3 py-1 rounded-lg">
                      {t('blogs.create.ask.ai')}
                    </div>
                  </button>
                </div>
              </FormErrorWrapper>

              <div className="form-group form-button">
                <input
                  type="submit"
                  name="signup"
                  id="signup"
                  className="btn_1"
                  style={
                    !loadingAI
                      ? {}
                      : {
                          opacity: 0.7,
                          cursor: 'not-allowed',
                        }
                  }
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
