import './CoursesSubsectionCreate.scss';
import CoursesSubsectionCreateForm from './courses-subsection-create-form/CoursesSubsectionCreateForm';

interface CoursesSubsectionCreateProps {
  courseId: number;
  refreshSubsections: Function;
  isOwner: boolean;
}

export default function CoursesSubsectionCreate({
  isOwner,
  ...props
}: CoursesSubsectionCreateProps) {
  return (
    <div className="accordion" id="subCreateAccordion">
      <div className="title-wrapper d-flex justify-content-between align-items-center">
        <h4 className="title sub-create-title">Course Outline</h4>
        {isOwner && (
          <button
            type="button"
            data-toggle="collapse"
            data-target="#subCreateContent"
            aria-expanded="true"
            aria-controls="subCreateContent"
            className="btn_1 sub-create-btn">
            Create
          </button>
        )}
      </div>
      {isOwner && (
        <div
          id="subCreateContent"
          className="collapse"
          aria-labelledby="headingOne"
          data-parent="#subCreateAccordion">
          <CoursesSubsectionCreateForm
            courseId={props.courseId}
            refreshSubsections={props.refreshSubsections}
          />
        </div>
      )}
    </div>
  );
}
