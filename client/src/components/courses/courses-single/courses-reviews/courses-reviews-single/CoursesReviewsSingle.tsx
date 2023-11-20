import { v4 as uuidV4 } from 'uuid';
import { IReview } from '../../../../../types/interfaces/IReview';
import colorStar from '../../../icons/color_star.svg';
import star from '../../../icons/star.svg';
import { MAX_STARS } from '../../courses-rating/CoursesRating';

interface CoursesReviewsSingleProps {
  review: IReview;
}

export default function CoursesReviewsSingle({
  review,
}: CoursesReviewsSingleProps) {
  return (
    <div className="comment-list">
      <div className="single-comment single-reviews justify-content-between d-flex">
        <div className="user justify-content-between d-flex">
          <div className="thumb">
            <img src="img/cource/cource_1.png" alt="" />
          </div>
          <div className="desc">
            <h5>
              <a>
                {review.user.firstname} (@{review.user.username})
              </a>
            </h5>
            <div className="rating">
              {[...new Array(review.stars)].map((_) => (
                <a>
                  <img src={colorStar} alt="" key={uuidV4()} />
                </a>
              ))}

              {[...new Array(MAX_STARS - review.stars)].map((_) => (
                <a>
                  <img src={star} alt="" key={uuidV4()} />
                </a>
              ))}
            </div>
            <p className="comment">{review.text}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
