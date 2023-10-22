import { useCallback, useMemo, useState } from 'react';
import { SubmitHandler } from 'react-hook-form';
import { v4 as uuidV4 } from 'uuid';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import * as paginationUtils from '../../../utils/page';
import { camelCaseToWords } from '../../../utils/stringUtils';
import './AdminTable.scss';
import AdminTablePagination from './admin-table-pagination/AdminTablePagination';
import AdminTableSearch, {
  AdminSearchValues,
} from './admin-table-search/AdminTableSearch';

export type TogglePageFunction = (page: number) => void;
export type SwitchPageFunction = () => void;

export type AdminTableProps = {
  tableName: string;
  list: IObjectWithId[];
  create: boolean;
};

function TableTitle(props: { tableName: string; isEmpty: boolean }) {
  return (
    <h4 className="text-blue h4">
      {props.tableName} Table{props.isEmpty && ' - No current records'}
    </h4>
  );
}

function CreateButton() {
  return (
    <span className="admin-btn btn_1" rel="content-y" role="button">
      <i className="fa fa-plus"></i> CREATE
    </span>
  );
}

const PAGE_SIZE = 5;

export default function AdminTable(props: AdminTableProps) {
  const [currentPage, setCurrentPage] = useState(1);

  const updatedList = useMemo(
    () =>
      props.list.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE),
    [props.list, currentPage]
  );

  const [pages, isEmpty] = useMemo(() => {
    const pages = Math.ceil(props.list.length / PAGE_SIZE);
    return [pages, pages === 0];
  }, [props.list]);

  const columns = Object.keys(props.list[0]);

  const validatePage = paginationUtils.validatePage.bind(null, pages);

  const togglePage: TogglePageFunction = useCallback(
    (page: number) => {
      setCurrentPage(validatePage(page));
    },
    [setCurrentPage, validatePage]
  );

  const previousPage: SwitchPageFunction = useCallback(() => {
    setCurrentPage((currentPage) => validatePage(currentPage - 1));
  }, [setCurrentPage, validatePage]);

  const nextPage: SwitchPageFunction = useCallback(() => {
    setCurrentPage((currentPage) => validatePage(currentPage + 1));
  }, [setCurrentPage, validatePage]);

  const onSearch: SubmitHandler<AdminSearchValues> = useCallback((v) => {
    // add handling
    console.log(v);

    console.log('submitting');
  }, []);

  return (
    <div className="pd-20 card-box mb-30 admin-table section_margin">
      <div className="clearfix mb-20 d-flex flex-column justify-content-center align-items-center">
        <TableTitle tableName={props.tableName} isEmpty={isEmpty} />
        {props.create && <CreateButton />}
      </div>
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              {columns.map((x) => (
                <th scope="col" key={x}>
                  {camelCaseToWords(x)}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {updatedList.map((x) => (
              <tr key={x.id}>
                {Object.values(x).map((x) => (
                  <td key={uuidV4()}>{x}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="d-flex flex-row justify-content-between">
        <AdminTablePagination
          currentPage={currentPage}
          pageCount={pages}
          togglePage={togglePage}
          previousPage={previousPage}
          nextPage={nextPage}
        />
        <AdminTableSearch onSubmit={onSearch} />
      </div>
    </div>
  );
}
