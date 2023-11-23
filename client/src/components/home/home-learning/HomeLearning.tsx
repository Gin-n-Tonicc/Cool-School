import { Link } from 'react-router-dom';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import '../styles/common_learning.scss';
import learningImg from './images/learning_img.png';

export default function HomeLearning() {
  return (
    <section className="learning_part">
      <div className="container">
        <div className="row align-items-sm-center align-items-lg-stretch">
          <div className="col-md-7 col-lg-7">
            <div className="learning_img">
              <img src={learningImg} alt="" />
            </div>
          </div>
          <div className="col-md-5 col-lg-5">
            <div className="learning_member_text">
              <h5>Our site</h5>
              <h2>Engage and Connect</h2>
              <p>
                Contrary to common misconceptions, online education is a dynamic
                and interactive experience. Engage with instructors and fellow
                students through courses, blogs, and projects. Forge connections
                that extend beyond geographical boundaries, creating a
                supportive community that enhances your learning experience.
              </p>
              <ul>
                <li>
                  <span className="ti-pencil-alt"></span>
                  Dynamic and interactive experience
                </li>
                <li>
                  <span className="ti-ruler-pencil"></span>
                  Create a supportive community that enhances your learning
                  experience
                </li>
              </ul>
              <Link to={PagesEnum.Blog} className="btn_1">
                Read More
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
