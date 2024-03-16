import { useTranslation } from 'react-i18next';
import { ICommentsByBlogResponse } from '../../../../types/interfaces/blogs/ICommentsByBlogResponse';
import SingleBlogComment from './single-blog-comment/SingleBlogComment';

interface SingleBlogCommentsProps extends ICommentsByBlogResponse {
  loadMoreComments: Function;
}

// The component that displays the most recent comments
export default function SingleBlogComments(props: SingleBlogCommentsProps) {
  const { t } = useTranslation();
  const areAllCommentsLoaded = props.comments.length === props.totalComments;

  return (
    <div className="comments-area">
      <h4>
        {props.totalComments} {t('blogs.comments')}
      </h4>
      {props.comments.map((x) => (
        <SingleBlogComment key={x.id} comment={x} />
      ))}

      {!areAllCommentsLoaded && (
        <button className="btn_1 mt-4" onClick={() => props.loadMoreComments()}>
          {t('blogs.comments.load.more')}
        </button>
      )}
    </div>
  );
}
