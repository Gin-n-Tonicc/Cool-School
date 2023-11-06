import { FormEventHandler } from 'react';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import { IComment } from '../../../../types/interfaces/IComment';

interface SingleBlogCommentFormProps {
  blogId: number;
  refreshComments: Function;
}

export default function SingleBlogCommentForm(
  props: SingleBlogCommentFormProps
) {
  const { user } = useAuthContext();
  const { post } = useFetch<IComment>(apiUrlsConfig.comments.post);

  const onSubmit: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
    const { comment } = Object.fromEntries(new FormData(form));

    await post({
      comment: ((comment as string) || '').trim(),
      ownerId: user.id,
      blogId: props.blogId,
      liked_users: [],
    });

    props.refreshComments();
    form.reset();
  };

  return (
    <div className="comment-form">
      <h4>Leave a Comment</h4>
      <form className="form-contact comment_form" onSubmit={onSubmit}>
        <div className="row">
          <div className="col-12">
            <div className="form-group">
              <textarea
                className="form-control w-100"
                name="comment"
                cols={30}
                rows={9}
                placeholder="Write Comment"></textarea>
            </div>
          </div>
        </div>
        <div className="form-group">
          <button type="submit" className="button btn_1 button-contactForm">
            Send Comment
          </button>
        </div>
      </form>
    </div>
  );
}
