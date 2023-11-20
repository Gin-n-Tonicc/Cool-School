import { IReview } from '../../../../types/interfaces/IReview';
import CoursesReviewsSingle from './courses-reviews-single/CoursesReviewsSingle';

interface CoursesReviewsProps {
  reviews: IReview[];
}

export default function CoursesReviews(props: CoursesReviewsProps) {
  return (
    <div className="comments-area mb-30">
      {props.reviews.map((x) => (
        <CoursesReviewsSingle review={x} />
      ))}
    </div>
  );
}
