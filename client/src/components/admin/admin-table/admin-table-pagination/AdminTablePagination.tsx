import { PaginationProps } from '../../../../hooks/usePagination';

interface AdminTablePaginationProps extends PaginationProps {}

// The component that displays the table pagination buttons
export default function AdminTablePagination(props: AdminTablePaginationProps) {
  return (
    <ul className="pagination admin-pagination">
      {/* Previous page button */}
      <li className="page-item">
        <a
          className="page-link"
          aria-label="Previous"
          onClick={() => props.previousPage()}>
          <span aria-hidden="true">&laquo;</span>
          <span className="sr-only">Previous</span>
        </a>
      </li>

      {/* Page number buttons */}
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

      {/* Next page button */}
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
