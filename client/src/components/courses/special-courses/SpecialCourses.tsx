import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import { usePagination } from '../../../hooks/usePagination';
import { ICourse } from '../../../types/interfaces/courses/ICourse';
import BlogPagination from '../../blog/blog-left-sidebar/blog-pagination/BlogPagination';
import './SpecialCourses.scss';
import HomeCourse from './special-course/SpecialCourse';

const PAGE_SIZE = 2;

// The component that displays {n} (paginated) amount of courses
export default function SpecialCourses() {
  const { t } = useTranslation();

  // Fetch all courses on mount
  const { data } = useFetch<ICourse[]>(apiUrlsConfig.courses.getAll, []);

  const {
    list: paginatedCourses,
    pages,
    currentPage,
    togglePage,
    nextPage,
    previousPage,
  } = usePagination<ICourse>(data, PAGE_SIZE);

  return (
    <section className="special_cource padding_top">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-xl-5">
            <div className="section_tittle text-center">
              <p>{t('courses.all')}</p>
              <h2>{t('courses.courses')}</h2>
            </div>
          </div>
        </div>
        <div className="row special-courses">
          {paginatedCourses?.map((x) => (
            <HomeCourse
              key={x.id}
              id={x.id}
              titleSummary={x.category.name}
              title={x.name}
              courseImage={apiUrlsConfig.files.getByUrl(x.picture.url)}
              author={`${x.user.firstname} (@${x.user.username})`}
              rating={Math.round((x.stars + Number.EPSILON) * 100) / 100}
            />
          ))}
        </div>
      </div>
      <div className="special-courses-pagination">
        <BlogPagination
          pages={pages}
          currentPage={currentPage}
          togglePage={togglePage}
          previousPage={previousPage}
          nextPage={nextPage}
        />
      </div>
    </section>
  );
}
