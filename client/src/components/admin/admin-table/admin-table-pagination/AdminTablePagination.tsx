import { PaginationProps } from '../../../../hooks/usePagination';

interface AdminTablePaginationProps extends PaginationProps {}

export default function AdminTablePagination(props: AdminTablePaginationProps) {
  return (
    <ul className="pagination admin-pagination">
      <li className="page-item">
        <a
          className="page-link"
          aria-label="Previous"
          onClick={() => props.previousPage()}>
          <span aria-hidden="true">&laquo;</span>
          <span className="sr-only">Previous</span>
        </a>
      </li>
      {[...Array(props.pages)].map((_, i) => {
        const page = i + 1;
        let classNames = 'page-link';

        if (props.currentPage === page) {
          classNames += ' active-page';
        }

        return (
          <li
            key={i}
            className="page-item"
            onClick={() => props.togglePage(page)}>
            <a className={classNames}>{page}</a>
          </li>
        );
      })}
      <li className="page-item">
        <a
          className="page-link"
          aria-label="Next"
          onClick={() => props.nextPage()}>
          <span aria-hidden="true">&raquo;</span>
          <span className="sr-only">Next</span>
        </a>
      </li>
    </ul>
  );
}
