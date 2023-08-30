import './SpecialCourses.scss';
import HomeCourse, { SpecialCourseProps } from './special-course/SpecialCourse';

export default function SpecialCourses() {
  const courses: SpecialCourseProps[] = [
    {
      titleSummary: 'Web Development',
      title: 'Web Development',
      price: 130,
      description:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      courseImage: require('./images/special_cource_1.png'),
      authorImage: require('./images/author_1.png'),
      author: 'James Well',
      rating: 3.8,
    },
    {
      titleSummary: 'Design',
      title: 'Web UX/UI Design',
      price: 160.0,
      description:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      courseImage: require('./images/special_cource_2.png'),
      authorImage: require('./images/author_2.png'),
      author: 'James Well',
      rating: 3.8,
    },
    {
      titleSummary: 'Wordpress',
      title: 'Wordpress Development',
      price: 140.0,
      description:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      courseImage: require('./images/special_cource_3.png'),
      authorImage: require('./images/author_3.png'),
      author: 'James Well',
      rating: 3.8,
    },
  ];

  return (
    <section className="special_cource padding_top">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-xl-5">
            <div className="section_tittle text-center">
              <p>popular courses</p>
              <h2>Special Courses</h2>
            </div>
          </div>
        </div>
        <div className="row">
          {courses.map((x) => (
            <HomeCourse {...x} key={x.title} />
          ))}
        </div>
      </div>
    </section>
  );
}
