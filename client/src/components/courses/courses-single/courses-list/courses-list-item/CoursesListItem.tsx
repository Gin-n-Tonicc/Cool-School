import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { v4 as uuidV4 } from 'uuid';
import { apiUrlsConfig } from '../../../../../config/apiUrls';
import { useFetch } from '../../../../../hooks/useFetch';
import { PagesEnum } from '../../../../../types/enums/PagesEnum';
import { ICourseSubsection } from '../../../../../types/interfaces/ICourseSubsection';
import { IResource } from '../../../../../types/interfaces/IResource';
import CoursesResourcesCreate from '../../courses-resources-create/CoursesResourcesCreate';
import './CoursesListItem.scss';
import CoursesListItemEdit from './courses-list-item-edit/CoursesListItemEdit';
import CoursesQuizzes from './courses-quizzes/CoursesQuizzes';
import CoursesResource from './courses-resource/CoursesResource';

interface CoursesListItemProps {
  subsection: ICourseSubsection;
  isOwner: boolean;
  hasEnrolled: boolean;
  refreshSubsections: Function;
  courseId: number;
}

export default function CoursesListItem({
  subsection,
  isOwner,
  hasEnrolled,
  refreshSubsections,
  courseId,
}: CoursesListItemProps) {
  const { t } = useTranslation();

  const { get, data: resources } = useFetch<IResource[]>(
    apiUrlsConfig.resources.getBySubsection(subsection.id),
    []
  );

  const { del, response: delResponse } = useFetch<IResource[]>(
    apiUrlsConfig.courseSubsections.delete(subsection.id)
  );

  const refreshResources = () => get();
  const collapseId = `collapse${uuidV4()}`;
  const editCollapseId = `collapseEdit${uuidV4()}`;

  const handleDelete = async () => {
    if (
      !window.confirm(
        `Are you sure you want to delete subsection \'${subsection.title}\'`
      )
    ) {
      return;
    }

    await del();
    if (delResponse.ok) {
      refreshSubsections();
    }
  };

  return (
    <li className="justify-content-between d-flex flex-column">
      {isOwner && (
        <div className="accordion" id="subControlAccordion">
          <div className="course-list-resource-control">
            <button className="btn_2 text-uppercase">
              <i onClick={handleDelete} className="fas fa-minus-circle"></i>
            </button>
            <button
              type="button"
              data-toggle="collapse"
              data-target={`#${editCollapseId}`}
              aria-expanded="true"
              aria-controls={editCollapseId}
              className="btn_2 text-uppercase">
              <i className="fas fa-pen"></i>
            </button>
          </div>
          <div
            id={editCollapseId}
            className="collapse"
            aria-labelledby="headingOne"
            data-parent="#subControlAccordion">
            <div className="card-body">
              <CoursesListItemEdit
                subsection={subsection}
                refreshSubsections={refreshSubsections}
                courseId={courseId}
              />
            </div>
          </div>
        </div>
      )}
      <div className="justify-content-between align-items-center d-flex">
        <p>{subsection.title}</p>
        {hasEnrolled && (
          <button
            type="button"
            data-toggle="collapse"
            data-target={`#${collapseId}`}
            aria-expanded="true"
            aria-controls={collapseId}
            className="btn_2 text-uppercase">
            {t('courses.view.details')}
          </button>
        )}
      </div>
      {hasEnrolled && (
        <div
          id={collapseId}
          className="collapse"
          aria-labelledby="headingOne"
          data-parent="#coursesAccordion">
          <div className="card-body">
            <div>
              <h5>{t('courses.subsection.description')}</h5>
              {subsection.description}
            </div>

            <div>
              <div className="title-wrapper d-flex justify-content-between align-items-center">
                <h5>Тестове</h5>

                {isOwner && (
                  <Link to={PagesEnum.Home} className="btn_1 res-create-btn">
                    {t('courses.resources.add.button')}
                  </Link>
                )}
              </div>
              <ul className="list-group"></ul>
              <CoursesQuizzes subsectionId={subsection.id} isOwner={isOwner} />
            </div>

            <div>
              <CoursesResourcesCreate
                subsectionId={subsection.id}
                refreshResources={refreshResources}
                isOwner={isOwner}
              />
              <ul className="list-group">
                {resources?.map((x) => (
                  <CoursesResource
                    key={x.id}
                    resource={x}
                    refreshResources={refreshResources}
                    isOwner={isOwner}
                  />
                ))}
              </ul>
            </div>
          </div>
        </div>
      )}
    </li>
  );
}
