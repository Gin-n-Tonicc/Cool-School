import { FormEventHandler } from 'react';
import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { useFetch } from '../../../../hooks/useFetch';
import { IComment } from '../../../../types/interfaces/blogs/IComment';

interface SingleBlogCommentFormProps {
  blogId: number;
  refreshComments: Function;
}

// The component that displays the comment creation form
// and handles it using a submit handler
export default function SingleBlogCommentForm(
  props: SingleBlogCommentFormProps
) {
  const { t } = useTranslation();
  const { user } = useAuthContext();

  // Prepare fetch
  const { response, post } = useFetch<IComment>(apiUrlsConfig.comments.post);

  // Handle form submit
  const onSubmit: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
    const { comment } = Object.fromEntries(new FormData(form));

    // Post the new comment
    await post({
      comment: ((comment as string) || '').trim(),
      ownerId: user.id,
      blogId: props.blogId,
      liked_users: [],
    });

    // Update the comments and reset the form
    if (response.ok) {
      props.refreshComments();
      form.reset();
    }
  };

  return (
    <div className="comment-form">
      <h4>{t('blogs.leave.comment')}</h4>
      <form className="form-contact comment_form" onSubmit={onSubmit}>
        <div className="row">
          <div className="col-12">
            <div className="form-group">
              <textarea
                className="form-control w-100"
                name="comment"
                cols={30}
                rows={9}
                placeholder={t('blogs.comments.write')}></textarea>
            </div>
          </div>
        </div>
        <div className="form-group">
          <button type="submit" className="button btn_1 button-contactForm">
            {t('blogs.comments.send')}
          </button>
        </div>
      </form>
    </div>
  );
}
