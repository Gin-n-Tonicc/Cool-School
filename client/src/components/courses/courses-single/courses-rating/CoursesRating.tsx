import { FormEventHandler, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useAuthContext } from '../../../../contexts/AuthContext';
import CoursesSingleRating from './courses-single-rating/CoursesSingleRating';

interface CoursesRatingProps {
  courseId: number;
  refreshReviews: Function;
  hasEnrolled: boolean;
}

export const MAX_STARS = 5;
export default function CoursesRating(props: CoursesRatingProps) {
  const { t } = useTranslation();
  const { user } = useAuthContext();
  const [qualityStars, setQualityStars] = useState(0);
  const [punctualityStars, setPunctualityStars] = useState(0);

  const avgStarRating = useMemo(
    () => Math.floor((qualityStars + punctualityStars) / 2),
    [qualityStars, punctualityStars]
  );

  const { post, response } = useFetch<any>(apiUrlsConfig.reviews.create);

  const onSubmit: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const form = e.currentTarget;
    const { feedback } = Object.fromEntries(new FormData(form));

    await post({
      text: ((feedback as string) || '').trim(),
      stars: avgStarRating,
      userId: user.id,
      courseId: props.courseId,
    });

    if (response.ok) {
      setQualityStars(0);
      setPunctualityStars(0);
      props.refreshReviews();
      form.reset();
    }
  };

  return (
    <>
      <div className="review-top row pt-40">
        <div className="col-lg-12">
          <h6 className="mb-15">{t('courses.provide.rating')}</h6>
          <CoursesSingleRating
            ratingName={t('courses.quality')}
            stars={qualityStars}
            setStars={setQualityStars}
            MAX_STARS={MAX_STARS}
          />
          <CoursesSingleRating
            ratingName={t('courses.punctuality')}
            stars={punctualityStars}
            setStars={setPunctualityStars}
            MAX_STARS={MAX_STARS}
          />
        </div>
      </div>
      <form onSubmit={onSubmit} className="feedeback">
        <h6>{t('courses.feedback')}</h6>
        <textarea
          name="feedback"
          className="form-control"
          cols={10}
          rows={10}></textarea>
        <div className="mt-10 text-right">
          {props.hasEnrolled && (
            <button type="submit" className="btn_1">
              {t('courses.subsection.submit.button')}
            </button>
          )}
        </div>
      </form>
    </>
  );
}
