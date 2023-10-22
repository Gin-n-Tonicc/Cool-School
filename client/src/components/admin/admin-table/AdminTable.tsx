import { useCallback, useEffect, useMemo, useState } from 'react';
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
  const [filteredList, setFilteredList] = useState(props.list);
  const [list, setList] = useState<IObjectWithId[]>(props.list);
  const [pages, setPages] = useState(1);

  useEffect(() => {
    setList(
      filteredList.slice((currentPage - 1) * PAGE_SIZE, currentPage * PAGE_SIZE)
    );
  }, [filteredList, currentPage]);

  useEffect(() => {
    const pages = Math.ceil(filteredList.length / PAGE_SIZE);
    setPages(pages);
  }, [filteredList]);

  const validatePage = useMemo(
    () => paginationUtils.validatePage.bind(null, pages),
    [pages]
  );

  useEffect(() => {
    setCurrentPage(validatePage(currentPage));
  }, [pages]);

  const columns = Object.keys(props.list[0]);
  const columnsLowercased = columns.map((x) => x.toLowerCase());

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
    const filteredList = props.list.filter((x) => {
      for (const key in x) {
        if (v[key]) {
          if (isNaN(x[key])) {
            const baseString = (x[key] as string).trim().toLowerCase();

            if (!baseString.includes(`${v[key]}`)) {
              return false;
            }
          } else if (v[key] !== x[key]) {
            return false;
          }
        }
      }

      return true;
    });

    setFilteredList(filteredList);
  }, []);

  return (
    <div className="pd-20 card-box mb-30 admin-table section_margin">
      <div className="clearfix mb-20 d-flex flex-column justify-content-center align-items-center">
        <TableTitle tableName={props.tableName} isEmpty={pages === 0} />
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
            {list.map((x) => (
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
        <AdminTableSearch
          onSubmit={onSearch}
          columnsLowercased={columnsLowercased}
        />
      </div>
    </div>
  );
}
