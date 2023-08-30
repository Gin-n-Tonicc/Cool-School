import '../styles/common_learning.scss';
import advanceFeatureImg from './images/advance_feature_img.png';

export default function HomeLearningFeature() {
  return (
    <section className="advance_feature learning_part">
      <div className="container">
        <div className="row align-items-sm-center align-items-xl-stretch">
          <div className="col-md-6 col-lg-6">
            <div className="learning_member_text">
              <h5>Advance feature</h5>
              <h2>Our Advance Educator Learning System</h2>
              <p>
                Fifth saying upon divide divide rule for deep their female all
                hath brind mid Days and beast greater grass signs abundantly
                have greater also use over face earth days years under brought
                moveth she star
              </p>
              <div className="row">
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-pencil-alt"></span>
                    <h4>Learn Anywhere</h4>
                    <p>
                      There earth face earth behold she star so made void two
                      given and also our
                    </p>
                  </div>
                </div>
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-stamp"></span>
                    <h4>Expert Teacher</h4>
                    <p>
                      There earth face earth behold she star so made void two
                      given and also our
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-lg-6 col-md-6">
            <div className="learning_img">
              <img src={advanceFeatureImg} alt="" />
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
