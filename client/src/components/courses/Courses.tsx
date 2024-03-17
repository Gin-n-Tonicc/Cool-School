import CoursesBreadcrumb from './courses-breadcrumb/CoursesBreadcrumb';
import SpecialCourses from './special-courses/SpecialCourses';

// The component that groups all of
// the components used in the courses page
export default function Courses() {
  return (
    <>
      <CoursesBreadcrumb />
      <SpecialCourses />
    </>
  );
}
