import './SpecialCourse.scss';
import colorStar from './images/color_star.svg';
import star from './images/star.svg';

export interface SpecialCourseProps {
  titleSummary: string;
  title: string;
  price: number;
  description: string;
  courseImage: any;
  authorImage: any;
  author: string;
  rating: number;
}

export default function SpecialCourse(props: SpecialCourseProps) {
  return (
    <div className="col-sm-6 col-lg-4">
      <div className="single_special_cource">
        <img src={props.courseImage} className="special_img" alt="" />
        <div className="special_cource_text">
          <a href="course-details.html" className="btn_4">
            {props.titleSummary}
          </a>
          <h4>${props.price.toFixed(2)}</h4>
          <a href="course-details.html">
            <h3>{props.title}</h3>
          </a>
          <p>{props.description}</p>
          <div className="author_info">
            <div className="author_img">
              <img src={props.authorImage} alt="" />
              <div className="author_info_text">
                <p>Conduct by:</p>
                <h5>
                  <a href="#">{props.author}</a>
                </h5>
              </div>
            </div>
            <div className="author_rating">
              <div className="rating">
                <a href="#">
                  <img src={colorStar} alt="" />
                </a>
                <a href="#">
                  <img src={colorStar} alt="" />
                </a>
                <a href="#">
                  <img src={colorStar} alt="" />
                </a>
                <a href="#">
                  <img src={colorStar} alt="" />
                </a>
                <a href="#">
                  <img src={star} alt="" />
                </a>
              </div>
              <p>{props.rating} Ratings</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
