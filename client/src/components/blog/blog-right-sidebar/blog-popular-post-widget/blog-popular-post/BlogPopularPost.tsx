import { Link } from 'react-router-dom';
import './BlogPopularPost.scss';

export interface BlogPopularPostProps {
  img: string;
  redirectUrl: string;
  date: string;
  title: string;
}

// The component displays a single recent blog based on the passed props
export default function BlogPopularPost(props: BlogPopularPostProps) {
  return (
    <div className="media post_item">
      <img src={props.img} alt="post" />
      <div className="media-body">
        <Link to={props.redirectUrl}>
          <h3>{props.title}</h3>
        </Link>
        <p>{props.date}</p>
      </div>
    </div>
  );
}
