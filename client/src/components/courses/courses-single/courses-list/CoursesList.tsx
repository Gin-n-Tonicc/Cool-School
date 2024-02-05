import { ICourse } from '../../../../types/interfaces/ICourse';
import { ICourseSubsection } from '../../../../types/interfaces/ICourseSubsection';
import CoursesListItem from './courses-list-item/CoursesListItem';

interface CoursesListProps {
  subsections: ICourseSubsection[];
  isOwner: boolean;
  hasEnrolled: boolean;
  refreshSubsections: Function;
  courseId: number;
  course: ICourse;
}

export default function CoursesList(props: CoursesListProps) {
  return (
    <>
      <div className="content accordion" id="coursesAccordion">
        <ul className="course_list">
          {props.subsections.map((x) => (
            <CoursesListItem
              key={x.id}
              subsection={x}
              isOwner={props.isOwner}
              hasEnrolled={props.hasEnrolled || props.isOwner}
              refreshSubsections={props.refreshSubsections}
              courseId={props.courseId}
              course={props.course}
            />
          ))}
        </ul>
      </div>
    </>
  );
}
