import { ICommentsByBlogResponse } from '../../../../types/interfaces/ICommentsByBlogResponse';
import SingleBlogComment from './single-blog-comment/SingleBlogComment';

interface SingleBlogCommentsProps extends ICommentsByBlogResponse {
  loadMoreComments: Function;
}

export default function SingleBlogComments(props: SingleBlogCommentsProps) {
  const areAllCommentsLoaded = props.comments.length === props.totalComments;

  return (
    <div className="comments-area">
      <h4>{props.totalComments} Comments</h4>
      {props.comments.map((x) => (
        <SingleBlogComment key={x.id} comment={x} />
      ))}

      {!areAllCommentsLoaded && (
        <button className="btn_1 mt-4" onClick={() => props.loadMoreComments()}>
          Load more
        </button>
      )}
    </div>
  );
}
