import './BlogPopularPost.scss';

export interface BlogPopularPostProps {
  img: string;
  redirectUrl: string;
  date: string;
  title: string;
}

export default function BlogPopularPost(props: BlogPopularPostProps) {
  return (
    <div className="media post_item">
      <img src={props.img} alt="post" />
      <div className="media-body">
        <a href={props.redirectUrl}>
          <h3>{props.title}</h3>
        </a>
        <p>{props.date}</p>
      </div>
    </div>
  );
}
