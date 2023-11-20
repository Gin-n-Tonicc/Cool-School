import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { usePagination } from '../../../hooks/usePagination';
import { ICourse } from '../../../types/interfaces/ICourse';
import BlogPagination from '../../blog/blog-right-sidebar/blog-pagination/BlogPagination';
import './SpecialCourses.scss';
import HomeCourse from './special-course/SpecialCourse';

const PAGE_SIZE = 2;
export default function SpecialCourses() {
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
              <p>all courses</p>
              <h2>Courses</h2>
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
