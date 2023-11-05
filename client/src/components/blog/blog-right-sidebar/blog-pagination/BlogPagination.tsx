import { PaginationProps } from '../../../../hooks/usePagination';
import './BlogPagination.scss';

interface BlogPaginationProps extends PaginationProps {}

export default function BlogPagination(props: BlogPaginationProps) {
  return (
    <nav className="blog-pagination justify-content-center d-flex">
      <ul className="pagination">
        <li className="page-item">
          <a
            href="#"
            className="page-link"
            aria-label="Previous"
            onClick={(e) => {
              e.preventDefault();
              props.previousPage();
            }}>
            <i className="ti-angle-left"></i>
          </a>
        </li>
        <li className="page-item">
          <a href="#" className="page-link">
            1
          </a>
        </li>
        <li className="page-item active">
          <a href="#" className="page-link">
            2
          </a>
        </li>
        <li className="page-item">
          <a
            href="#"
            className="page-link"
            aria-label="Next"
            onClick={(e) => {
              e.preventDefault();
              props.nextPage();
            }}>
            <i className="ti-angle-right"></i>
          </a>
        </li>
      </ul>
    </nav>
  );
}
