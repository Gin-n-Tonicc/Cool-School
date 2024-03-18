import { IComment } from '../../../../../types/interfaces/blogs/IComment';

interface SingleBlogCommentProps {
  comment: IComment;
}

// This component displays a single comment based on the passed props
export default function SingleBlogComment(props: SingleBlogCommentProps) {
  // Extract date data
  const date = new Date(props.comment.created_at);
  const month = date.toLocaleString('default', { month: 'long' });
  const day = date.getDate();
  const year = date.getFullYear();
  const time = date.toLocaleTimeString();

  return (
    <div className="comment-list">
      <div className="single-comment justify-content-between d-flex">
        <div className="user justify-content-between d-flex">
          <div className="desc">
            <p className="comment">{props.comment.comment}</p>
            <div className="d-flex justify-content-between">
              <div className="d-flex align-items-center">
                <h5>
                  <span>
                    {props.comment.owner.firstname} (
                    {props.comment.owner.username})
                  </span>
                </h5>
                <p className="date">
                  {month} {day}, {year} at {time}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
