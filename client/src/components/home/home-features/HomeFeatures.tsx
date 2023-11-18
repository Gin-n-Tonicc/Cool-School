import './HomeFeatures.scss';
import HomeFeature from './home-feature/HomeFeature';

export default function HomeFeatures() {
  return (
    <section className="feature_part">
      <div className="container">
        <div className="row">
          <div className="col-sm-6 col-xl-3 align-self-center">
            <div className="single_feature_text ">
              <h3>
                What makes it cool?<br />
              </h3>
              <p>
                Here are 3 features that make the Cool School really cool for all students.
              </p>
              <a href="#" className="btn_1">
                Read More
              </a>
            </div>
          </div>
          <HomeFeature
            iconClassName="ti-layers"
            featureTitle="FLEXIBILITY"
            featureDescription="Unlock flexible learning with our online school—study when and where it suits you. Study everytime, everywhere!"
          />

          <HomeFeature
            iconClassName="ti-new-window"
            featureTitle="Personalized Pathways"
            featureDescription="Tailor your education to fit your lifestyle with our personalized online courses."
          />

          <HomeFeature
            iconClassName="ti-light-bulb"
            featureTitle="Anywhere Learning Hub"
            featureDescription="Seamless access to quality education, anytime, anywhere. Join us today!"
          />
        </div>
      </div>
    </section>
  );
}
