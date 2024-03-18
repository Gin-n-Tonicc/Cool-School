import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import './BlogItem.scss';

export interface BlogItemProps {
  imgUrl: string;
  title: string;
  summary: string;
  category: string;
  commentCount: number;
  date: Date;
  id: number;
}

// Single blog component
export default function BlogItem(props: BlogItemProps) {
  const { t } = useTranslation();

  // Get month and day from date
  const month = props.date.toLocaleString('en-US', { month: 'short' });
  const day = props.date.getDate();

  return (
    <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src={apiUrlsConfig.files.getByUrl(props.imgUrl)}
          alt=""
        />
        <span className="blog_item_date">
          <h3>{day}</h3>
          <p>{month}</p>
        </span>
      </div>

      {/* Show blog data */}
      <div className="blog_details">
        {/* Link to single blog page */}
        <Link
          className="d-inline-block"
          to={PagesEnum.SingleBlog.replace(':id', props.id.toString())}>
          <h2>{props.title}</h2>
        </Link>
        <p>{props.summary}</p>
        <ul className="blog-info-link">
          <li>
            <span>{props.category}</span>
          </li>
          <li>
            <span>
              <i className="far fa-comments"></i>
              {props.commentCount?.toString().padStart(2, '0')}{' '}
              {t('blogs.comments')}
            </span>
          </li>
        </ul>
      </div>
    </article>
  );
}
