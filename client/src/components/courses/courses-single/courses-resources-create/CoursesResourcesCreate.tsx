import { useTranslation } from 'react-i18next';
import './CoursesResourcesCreate.scss';
import CoursesResourcesCreateForm from './courses-resources-create-form/CoursesResourcesCreateForm';

interface CoursesSubsectionCreateProps {
  subsectionId: number;
  refreshResources: Function;
  isOwner: boolean;
}

// The component that displays the courses
// resources create form using an accordion
export default function CoursesResourcesCreate({
  isOwner,
  ...props
}: CoursesSubsectionCreateProps) {
  const { t } = useTranslation();

  return (
    <div className="accordion" id="resCreateAccordion">
      <div className="title-wrapper d-flex justify-content-between align-items-center">
        <h5>{t('courses.resources')}</h5>

        {isOwner && (
          <button
            type="button"
            data-toggle="collapse"
            data-target="#resCreateContent"
            aria-expanded="true"
            aria-controls="resCreateContent"
            className="btn_1 res-create-btn">
            {t('courses.resources.add.button')}
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
