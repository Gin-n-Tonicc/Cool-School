import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import './HomeBlogCard.scss';

export interface HomeBlogCardProps {
  id: number;
  category: string;
  title: string;
  summary: string;
  totalComments: number;
  totalLikes: number;
  image: string;
}

// The component that displays a single most popular blog
export default function HomeBlogCard(props: HomeBlogCardProps) {
  const { t } = useTranslation();

  return (
    <div className="col-sm-6 col-lg-4 col-xl-4 single-home-blog-wrapper">
      <div className="single-home-blog">
        <div className="card">
          <img src={props.image} className="card-img-top" alt="blog" />
          <div className="card-body">
            <div className="home-blog-summary">
              <a className="btn_4">{props.category}</a>
              <Link
                to={PagesEnum.SingleBlog.replace(':id', props.id.toString())}>
                <h5 className="card-title">{props.title}</h5>
              </Link>
              <p>{props.summary}</p>
            </div>
            <ul>
              <li>
                <span className="ti-comments"></span>
                {props.totalComments} {t('blogs.comments')}
              </li>
              <li>
                <span className="ti-heart"></span>
                {props.totalLikes} {t('blogs.likes')}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
