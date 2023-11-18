import '../styles/common_learning.scss';
import advanceFeatureImg from './images/advance_feature_img.png';

export default function HomeLearningFeature() {
  return (
    <section className="advance_feature learning_part">
      <div className="container">
        <div className="row align-items-sm-center align-items-xl-stretch">
          <div className="col-md-6 col-lg-6">
            <div className="learning_member_text">
              <h5>Personalized Progress</h5>
              <h2>Learning environment that adapts to you</h2>
              <p>
                Online schools recognize that every student is unique, offering personalized
                learning experiences that cater to individual strengths and preferences. Say
                goodbye to the frustration of moving at someone else's pace and hello to a customized
                educational journey that fits you like a glove.
              </p>
              <div className="row">
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-pencil-alt"></span>
                    <h4>Learn Anywhere</h4>
                    <p>
                      Learn Anywhere with our courses that can be reached at any time!
                    </p>
                  </div>
                </div>
                <div className="col-sm-6 col-md-12 col-lg-6">
                  <div className="learning_member_text_iner">
                    <span className="ti-stamp"></span>
                    <h4>Add something form yourself</h4>
                    <p>
                      With our functionality to create blogs, comment and like them you can add something for your digital school from yourself!
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
