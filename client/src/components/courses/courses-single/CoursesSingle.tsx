import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { ICourse } from '../../../types/interfaces/ICourse';
import { IReview } from '../../../types/interfaces/IReview';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';
import Spinner from '../../common/spinner/Spinner';
import './CoursesSingle.scss';
import CoursesRating from './courses-rating/CoursesRating';
import CoursesReviews from './courses-reviews/CoursesReviews';

export default function CoursesSingle() {
  const { id } = useParams();
  const { user } = useAuthContext();

  const { data: course, loading: courseLoading } = useFetch<ICourse>(
    apiUrlsConfig.courses.getOne(id),
    []
  );

  const numId = Number(id || -1);

  const { get: getCanEnroll, data: canEnrollReq } = useFetch<boolean>(
    apiUrlsConfig.courses.canEnroll(numId)
  );

  const { get: enrollCourse } = useFetch<void>(
    apiUrlsConfig.courses.enroll(numId)
  );

  const { get: getReviews, data: reviews } = useFetch<IReview[]>(
    apiUrlsConfig.reviews.getByCourse(numId),
    [course]
  );

  useEffect(() => {
    (async () => {
      if (!courseLoading) {
        await getCanEnroll();
      }
    })();
  }, [courseLoading]);

  const onEnroll = async () => {
    await enrollCourse();
    await getCanEnroll();
  };

  const refreshReviews = () => getReviews();

  if (!course) {
    return <Spinner />;
  }

  const canEnroll = user && user.id !== course.user.id && canEnrollReq;

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
                <h4 className="title_top">Objectives</h4>
                <div className="content">
                  {course.objectives
                    .split('\n')
                    .filter((x) => x)
                    .map((x) => {
                      return (
                        <>
                          {x.trim()}
                          <br />
                        </>
                      );
                    })}
                </div>

                <h4 className="title">Eligibility</h4>
                <div className="content">
                  {course.eligibility
                    .split('\n')
                    .filter((x) => x)
                    .map((x) => {
                      return (
                        <>
                          {x.trim()}
                          <br />
                        </>
                      );
                    })}
                </div>

                <h4 className="title">Course Outline</h4>
                <div className="content">
                  <ul className="course_list">
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Introduction Lesson</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Basics of HTML</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Getting Know about HTML</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Tags and Attributes</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Basics of CSS</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Getting Familiar with CSS</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Introduction to Bootstrap</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Responsive Design</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                    <li className="justify-content-between align-items-center d-flex">
                      <p>Canvas in HTML 5</p>
                      <a className="btn_2 text-uppercase" href="#">
                        View Details
                      </a>
                    </li>
                  </ul>
                </div>
              </div>
            </div>

            <div className="col-lg-4 right-contents">
              <div className="sidebar_top">
                <ul>
                  <li>
                    <a className="justify-content-between d-flex">
                      <p>Course name</p>
                      <span className="color">{course.name}</span>
                    </a>
                  </li>
                  <li>
                    <a className="justify-content-between d-flex">
                      <p>Trainer’s Name</p>
                      <span className="color">
                        {course.user.firstname} (@{course.user.username})
                      </span>
                    </a>
                  </li>
                </ul>
                {canEnroll && (
                  <button className="btn_1 d-block" onClick={onEnroll}>
                    Enroll the course
                  </button>
                )}
              </div>

              <h4 className="title">Reviews</h4>
              <div className="content">
                <CoursesRating
                  courseId={course.id}
                  refreshReviews={refreshReviews}
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
