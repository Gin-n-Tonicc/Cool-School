import './BlogItem.scss';

export interface BlogItemProps {
  img: string;
  title: string;
  summary: string;
  category: string;
  commentCount: number;
  date: Date;
}

export default function BlogItem(props: BlogItemProps) {
  const month = props.date.toLocaleString('default', { month: 'short' });
  const day = props.date.getDate();

  return (
    <article className="blog_item">
      <div className="blog_item_img">
        <img className="card-img rounded-0" src={props.img} alt="" />
        <span className="blog_item_date">
          <h3>{day}</h3>
          <p>{month}</p>
        </span>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>{props.title}</h2>
        </a>
        <p>{props.summary}</p>
        <ul className="blog-info-link">
          <li>
            <span>{props.category}</span>
          </li>
          <li>
            <span>
              <i className="far fa-comments"></i>
              {props.commentCount?.toString().padStart(2, '0')} Comments
            </span>
          </li>
        </ul>
      </div>
    </article>
  );
}
