import './HomeFeature.scss';

export interface HomeFeatureProps {
  iconClassName: string;
  featureTitle: string;
  featureDescription: string;
}

// The component that displays one single learning features
export default function HomeFeature(props: HomeFeatureProps) {
  return (
    <div className="col-sm-6 col-xl-3">
      <div className="single_feature">
        <div className="single_feature_part">
          <span className="single_feature_icon">
            <i className={props.iconClassName}></i>
          </span>
          <h4>{props.featureTitle}</h4>
          <p>{props.featureDescription}</p>
        </div>
      </div>
    </div>
  );
}
