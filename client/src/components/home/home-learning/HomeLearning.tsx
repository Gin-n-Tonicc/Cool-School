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
              <h5>About us</h5>
              <h2>Learning with Love and Laughter</h2>
              <p>
                Fifth saying upon divide divide rule for deep their female all
                hath brind Days and beast greater grass signs abundantly have
                greater also days years under brought moveth.
              </p>
              <ul>
                <li>
                  <span className="ti-pencil-alt"></span>Him lights given i
                  heaven second yielding seas gathered wear
                </li>
                <li>
                  <span className="ti-ruler-pencil"></span>Fly female them
                  whales fly them day deep given night.
                </li>
              </ul>
              <a href="#" className="btn_1">
                Read More
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
