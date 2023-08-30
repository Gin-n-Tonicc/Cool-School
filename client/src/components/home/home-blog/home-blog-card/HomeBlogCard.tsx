import './HomeBlogCard.scss';

export interface HomeBlogCardProps {
  category: string;
  title: string;
  summary: string;
  totalComments: number;
  totalLikes: number;
  image: string;
}

export default function HomeBlogCard(props: HomeBlogCardProps) {
  return (
    <div className="col-sm-6 col-lg-4 col-xl-4">
      <div className="single-home-blog">
        <div className="card">
          <img src={props.image} className="card-img-top" alt="blog" />
          <div className="card-body">
            <a href="#" className="btn_4">
              {props.category}
            </a>
            <a href="blog.html">
              <h5 className="card-title">{props.title}</h5>
            </a>
            <p>{props.summary}</p>
            <ul>
              <li>
                <span className="ti-comments"></span>
                {props.totalComments} Comments
              </li>
              <li>
                <span className="ti-heart"></span>
                {props.totalLikes} Likes
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
