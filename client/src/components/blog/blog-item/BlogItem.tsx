import './BlogItem.scss';

export interface BlogItemProps {
  img: string;
  monthDay: number;
  month: string;
  title: string;
  description: string;
  category: string;
  commentCount: number;
}

export default function BlogItem(props: BlogItemProps) {
  return (
    <article className="blog_item">
      <div className="blog_item_img">
        <img className="card-img rounded-0" src={props.img} alt="" />
        <a href="#" className="blog_item_date">
          <h3>{props.monthDay}</h3>
          <p>{props.month}</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>{props.title}</h2>
        </a>
        <p>{props.description}</p>
        <ul className="blog-info-link">
          <li>
            <a href="#">{props.category}</a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i>
              {props.commentCount.toString().padStart(2, '0')} Comments
            </a>
          </li>
        </ul>
      </div>
    </article>
  );
}
