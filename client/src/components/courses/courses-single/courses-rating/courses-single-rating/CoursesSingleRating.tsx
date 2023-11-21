import { SetStateAction } from 'react';
import { v4 as uuidV4 } from 'uuid';
import colorStar from '../../../icons/color_star.svg';
import star from '../../../icons/star.svg';

interface CoursesSingleRatingProps {
  ratingName: string;
  stars: number;
  setStars: React.Dispatch<SetStateAction<number>>;
  MAX_STARS: number;
}

export default function CoursesSingleRating({
  stars,
  setStars,
  ratingName,
  MAX_STARS,
}: CoursesSingleRatingProps) {
  const onStarChange = (newStars: number) => {
    if (newStars > MAX_STARS) {
      return setStars(MAX_STARS);
    }

    if (newStars < 0) {
      return setStars(0);
    }

    setStars((oldStars) => {
      if (oldStars === newStars) {
        return 0;
      }

      return newStars;
    });
  };

  let counter = 0;
  const starsArr = [...new Array(MAX_STARS)].map((_, i) => {
    if (counter < stars) {
      counter++;
      return (
        <a key={uuidV4()}>
          <img
            src={colorStar}
            alt=""
            onClick={onStarChange.bind(null, i + 1)}
          />
        </a>
      );
    }

    return (
      <a key={uuidV4()}>
        <img src={star} alt="" onClick={onStarChange.bind(null, i + 1)} />
      </a>
    );
  });

  return (
    <div className="d-flex flex-row reviews justify-content-between">
      <span>{ratingName}</span>
      <div className="rating">{starsArr}</div>
      <span>Outstanding</span>
    </div>
  );
}
