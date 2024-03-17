import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { v4 as uuidV4 } from 'uuid';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { MAX_STARS } from '../../courses-single/courses-rating/CoursesRating';
import './SpecialCourse.scss';
import colorStar from './images/color_star.svg';
import star from './images/star.svg';

export interface SpecialCourseProps {
  id: number;
  titleSummary: string;
  title: string;
  price?: number;
  description?: string;
  courseImage: any;
  authorImage?: any;
  author: string;
  rating: number;
}

// The component that displays a brief summary of a single course
export default function SpecialCourse(props: SpecialCourseProps) {
  const { t } = useTranslation();
  const stars = Math.round(props.rating);

  return (
    <div className="col-sm-6 col-lg-4 special_course_parent">
      <div className="single_special_cource">
        <img src={props.courseImage} className="special_img" alt="" />
        <div className="special_cource_text">
          <Link
            to={PagesEnum.SingleCourse.replace(':id', props.id.toString())}
            className="btn_4">
            {props.titleSummary}
          </Link>
          <Link to={PagesEnum.SingleCourse.replace(':id', props.id.toString())}>
            <h3>{props.title}</h3>
          </Link>
          <div className="author_info">
            <div className="author_img">
              <div className="author_info_text">
                <p>{t('courses.conduct.by')}</p>
                <h5>
                  <a>{props.author}</a>
                </h5>
              </div>
            </div>
            <div className="author_rating">
              <div className="rating">
                {/* Given stars */}
                {[...new Array(stars)].map((_) => (
                  <a key={uuidV4()}>
                    <img src={colorStar} alt="" />
                  </a>
                ))}

                {/* Remaining stars */}
                {[...new Array(MAX_STARS - stars)].map((_) => (
                  <a key={uuidV4()}>
                    <img src={star} alt="" />
                  </a>
                ))}
              </div>
              <p>
                {props.rating} {t('courses.ratings')}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
