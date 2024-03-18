import { Fragment, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useParams } from 'react-router-dom';
import { v4 as uuidV4 } from 'uuid';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { useFetch } from '../../../hooks/useFetch';
import { ICourse } from '../../../types/interfaces/courses/ICourse';
import { ICourseSubsection } from '../../../types/interfaces/courses/ICourseSubsection';
import { IReview } from '../../../types/interfaces/courses/IReview';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';
import './CoursesSingle.scss';
import CoursesList from './courses-list/CoursesList';
import CoursesRating from './courses-rating/CoursesRating';
import CoursesReviews from './courses-reviews/CoursesReviews';
import CoursesSubsectionCreate from './courses-subsection-create/CoursesSubsectionCreate';

// A component that displays the information about a single course
export default function CoursesSingle() {
  const { t } = useTranslation();
  const { id } = useParams();
  const { user, isAuthenticated } = useAuthContext();

  // Fetch course data on load
  const { data: course, loading: courseLoading } = useFetch<ICourse>(
    apiUrlsConfig.courses.getOne(id),
    []
  );

  const numId = Number(id || -1);

  // Prepare enroll fetches
  const { get: getCanEnroll, data: canEnrollReq } = useFetch<boolean>(
    apiUrlsConfig.courses.canEnroll(numId)
  );

  const { get: enrollCourse } = useFetch<void>(
    apiUrlsConfig.courses.enroll(numId)
  );

  // Fetch reviews on mount and course change
  const { get: getReviews, data: reviews } = useFetch<IReview[]>(
    apiUrlsConfig.reviews.getByCourse(numId),
    [course]
  );

  // Fetch subsections on mount and course change
  const { get: getSubsections, data: subsections } = useFetch<
    ICourseSubsection[]
  >(apiUrlsConfig.courseSubsections.getByCourse(numId), [course]);

  // On course load check if user has enrolled
  useEffect(() => {
    (async () => {
      if (!courseLoading) {
        await getCanEnroll();
      }
    })();
  }, [courseLoading]);

  if (!course) {
    return <></>;
  }

  // Prepare methods to be passed as props
  const onEnroll = async () => {
    await enrollCourse();
    await getCanEnroll();
  };

  const refreshReviews = () => getReviews();
  const refreshSubsections = () => getSubsections();

  const isOwner = isAuthenticated && user.id === course.user.id;
  const canEnroll = isAuthenticated && !isOwner && canEnrollReq;
  const hasEnrolled = !canEnrollReq && isAuthenticated;

  return (
    <>
      <Breadcrumb heading={course.name} pageName="Course" />
      <section className="course_details_area section_padding">
        <div className="container">
          <div className="row">
            <div className="col-lg-8 course_details_left">
              <div className="main_image">
                <img
                  className="img-fluid"
                  src={apiUrlsConfig.files.getByUrl(course.picture.url)}
                  alt=""
                />
              </div>
              <div className="content_wrapper">
                <h4 className="title_top">{t('courses.objectives')}</h4>
                <div className="content">
                  {course.objectives
                    .split('\n')
                    .filter((x) => x)
                    .map((x) => {
                      return (
                        <Fragment key={uuidV4()}>
                          {x.trim()}
                          <br />
                        </Fragment>
                      );
                    })}
                </div>

                <h4 className="title">{t('courses.eligibility')}</h4>
                <div className="content">
                  {course.eligibility
                    .split('\n')
                    .filter((x) => x)
                    .map((x) => {
                      return (
                        <Fragment key={uuidV4()}>
                          {x.trim()}
                          <br />
                        </Fragment>
                      );
                    })}
                </div>

                <CoursesSubsectionCreate
                  courseId={numId}
                  refreshSubsections={refreshSubsections}
                  isOwner={isOwner}
                />
                <CoursesList
                  course={course}
                  subsections={subsections || []}
                  isOwner={isOwner}
                  hasEnrolled={hasEnrolled}
                  refreshSubsections={refreshSubsections}
                  courseId={numId}
                />
              </div>
            </div>

            <div className="col-lg-4 right-contents">
              <div className="sidebar_top">
                <ul>
                  <li>
                    <a className="justify-content-between d-flex">
                      <p>{t('courses.name')}</p>
                      <span className="color">{course.name}</span>
                    </a>
                  </li>
                  <li>
                    <a className="justify-content-between d-flex">
                      <p>{t('courses.trainer.name')}</p>
                      <span className="color">
                        {course.user.firstname} (@{course.user.username})
                      </span>
                    </a>
                  </li>
                </ul>
                {canEnroll && (
                  <button className="btn_1 d-block" onClick={onEnroll}>
                    {t('courses.enroll')}
                  </button>
                )}
              </div>

              <h4 className="title">{t('courses.reviews')}</h4>
              <div className="content">
                <CoursesRating
                  courseId={course.id}
                  refreshReviews={refreshReviews}
                  hasEnrolled={hasEnrolled}
                />
                <CoursesReviews reviews={reviews || []} />
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
