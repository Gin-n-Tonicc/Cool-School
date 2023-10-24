import { useCallback, useEffect, useMemo, useState } from 'react';
import { SubmitHandler } from 'react-hook-form';
import { v4 as uuidV4 } from 'uuid';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import * as paginationUtils from '../../../utils/page';
import { camelCaseToWords } from '../../../utils/stringUtils';
import AdminEditForm, {
  OnUpdateFunction,
} from '../admin-edit-form/AdminEditForm';
import './AdminTable.scss';
import AdminTablePagination, {
  SwitchPageFunction,
  TogglePageFunction,
} from './admin-table-pagination/AdminTablePagination';
import AdminTableSearch, {
  AdminSearchValues,
} from './admin-table-search/AdminTableSearch';

export type OnDeleteFunction = (id: number) => void;

interface AdminTableProps {
  tableName: string;
  list: IObjectWithId[];
  create: boolean;
  update: boolean;
  delete: boolean;
  onDelete: OnDeleteFunction;
  onUpdate: OnUpdateFunction;
}

interface AdminTableTitleProps {
  tableName: string;
  isEmpty: boolean;
}

function TableTitle(props: AdminTableTitleProps) {
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
  const [isEditing, setIsEditing] = useState(false);
  const [currentObj, setCurrentObj] = useState<IObjectWithId>({ id: -1 });

  const [filteredList, setFilteredList] = useState(props.list);
  const [list, setList] = useState<IObjectWithId[]>(props.list);
  const [currentPage, setCurrentPage] = useState(1);
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

  const [columns, columnsLowercased] = useMemo(() => {
    let columns: string[];

    try {
      columns = Object.keys(props.list[0]);
    } catch {
      columns = [];
    }

    return [columns, columns.map((x) => x.toLowerCase())];
  }, [props.list]);

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

  const openEdit = () => {
    setIsEditing(true);
  };

  const closeEdit = () => {
    setIsEditing(false);
  };

  const onUpdate = useCallback(
    (id: number) => {
      if (isEditing && currentObj.id === id) {
        return closeEdit();
      }

      const foundObj = props.list.find((x) => x.id === id);

      if (foundObj) {
        openEdit();
        setCurrentObj(foundObj);
      } else {
        closeEdit();
      }
    },
    [isEditing, currentObj]
  );

  const onDelete = useCallback((id: number) => {
    const confirmation = window.confirm(
      `Are you sure you want to delete row with ID=${id}?`
    );

    if (confirmation) {
      props.onDelete(id);
    }
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
              <tr className="table-admin-row" key={x.id}>
                {Object.values(x).map((x) => (
                  <td key={uuidV4()}>{`${x}`}</td>
                ))}
                <td className="control-buttons">
                  {props.update && (
                    <a onClick={onUpdate.bind(null, x.id)}>
                      <i className="fas fa-pen"></i>
                    </a>
                  )}

                  {props.delete && (
                    <a onClick={onDelete.bind(null, x.id)}>
                      <i className="fas fa-minus-circle"></i>
                    </a>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div>
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
        {isEditing && (
          <AdminEditForm
            currentObj={currentObj}
            columns={columns.filter((x) => x !== 'id')}
            onUpdate={props.onUpdate}
            closeEdit={closeEdit}
          />
        )}
      </div>
    </div>
  );
}
