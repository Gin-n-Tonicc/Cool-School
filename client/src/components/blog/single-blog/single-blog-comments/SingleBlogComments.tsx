import { IComment } from '../../../../types/interfaces/IComment';
import SingleBlogComment from './single-blog-comment/SingleBlogComment';

interface SingleBlogCommentsProps {
  comments: IComment[];
}

export default function SingleBlogComments(props: SingleBlogCommentsProps) {
  return (
    <div className="comments-area">
      <h4>{props.comments.length} Comments</h4>
      {props.comments.map((x) => (
        <SingleBlogComment key={x.id} comment={x} />
      ))}
    </div>
  );
}
