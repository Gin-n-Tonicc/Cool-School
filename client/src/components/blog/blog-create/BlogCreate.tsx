import { SubmitHandler, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IBlog } from '../../../types/interfaces/IBlog';
import { IFile } from '../../../types/interfaces/IFile';
import {
  CONTENT_VALIDATIONS,
  FILE_VALIDATIONS,
  SUMMARY_VALIDATIONS,
  TITLE_VALIDATIONS,
} from '../../../validations/blogCreateValidations';
import FormErrorWrapper from '../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../common/form-input/FormInput';
import './BlogCreate.scss';

type Inputs = {
  Title: string;
  Summary: string;
  content: string;
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
      content: '',
      file: [],
    },
    mode: 'onChange',
  });

  const values = watch();
  const navigate = useNavigate();

  const { post: filePost, response: postFileRes } = useFetch<IFile>(
    apiUrlsConfig.files.upload()
  );

  const { post: blogPost, response: postBlogRes } = useFetch<IBlog>(
    apiUrlsConfig.blogs.upload
  );

  const labelText = values.file[0]?.name || 'Choose blog image';

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
      categoryId: 1,
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
                rules={TITLE_VALIDATIONS}
              />

              <FormInput
                control={control}
                name="Summary"
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={SUMMARY_VALIDATIONS}
              />

              <FormErrorWrapper message={errors.content?.message}>
                <div className="blog-create-textarea-wrapper">
                  <h5>Blog content</h5>
                  <textarea
                    className="form-control"
                    {...register('content', { ...CONTENT_VALIDATIONS })}
                    rows={3}></textarea>
                </div>
              </FormErrorWrapper>

              <FormErrorWrapper message={errors.file?.message}>
                <div className="custom-file">
                  <input
                    type="file"
                    className="custom-file-input"
                    {...register('file', { ...FILE_VALIDATIONS })}
                  />
                  <label className="custom-file-label">{labelText}</label>
                </div>
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
