import './HomeFeatures.scss';
import HomeFeature from './home-feature/HomeFeature';

export default function HomeFeatures() {
  return (
    <section className="feature_part">
      <div className="container">
        <div className="row">
          <div className="col-sm-6 col-xl-3 align-self-center">
            <div className="single_feature_text ">
              <h2>
                Awesome <br /> Feature
              </h2>
              <p>
                Set have great you male grass yielding an yielding first their
                you're have called the abundantly fruit were man{' '}
              </p>
              <a href="#" className="btn_1">
                Read More
              </a>
            </div>
          </div>
          <HomeFeature
            iconClassName="ti-layers"
            featureTitle="Better Future"
            featureDescription="Set have great you male grasses yielding yielding first their
            to called deep abundantly Set have great you male"
          />

          <HomeFeature
            iconClassName="ti-new-window"
            featureTitle="Qualified Trainers"
            featureDescription="Set have great you male grasses yielding yielding first their
            to called deep abundantly Set have great you male"
          />

          <HomeFeature
            iconClassName="ti-light-bulb"
            featureTitle="Job Oppurtunity"
            featureDescription="Set have great you male grasses yielding yielding first their
            to called deep abundantly Set have great you male"
          />
        </div>
      </div>
    </section>
  );
}
