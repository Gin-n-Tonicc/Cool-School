import './CoursesResourcesCreate.scss';
import CoursesResourcesCreateForm from './courses-resources-create-form/CoursesResourcesCreateForm';

interface CoursesSubsectionCreateProps {
  subsectionId: number;
  refreshResources: Function;
  isOwner: boolean;
}

export default function CoursesResourcesCreate({
  isOwner,
  ...props
}: CoursesSubsectionCreateProps) {
  return (
    <div className="accordion" id="resCreateAccordion">
      <div className="title-wrapper d-flex justify-content-between align-items-center">
        <h5>Resources</h5>

        {isOwner && (
          <button
            type="button"
            data-toggle="collapse"
            data-target="#resCreateContent"
            aria-expanded="true"
            aria-controls="resCreateContent"
            className="btn_1 res-create-btn">
            Add
          </button>
        )}
      </div>
      {isOwner && (
        <div
          id="resCreateContent"
          className="collapse"
          aria-labelledby="headingOne"
          data-parent="#resCreateAccordion">
          <CoursesResourcesCreateForm
            subsectionId={props.subsectionId}
            refreshResources={props.refreshResources}
          />
        </div>
      )}
    </div>
  );
}
