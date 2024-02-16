import { useCallback, useMemo, useState } from 'react';
import { SubmitHandler } from 'react-hook-form';
import { v4 as uuidV4 } from 'uuid';
import { usePagination } from '../../../hooks/usePagination';
import { IObjectWithId } from '../../../types/interfaces/common/IObjectWithId';
import { camelCaseToWords } from '../../../utils/stringUtils';
import AdminEditForm, {
  OnCreateFunction,
  OnUpdateFunction,
} from '../admin-edit-form/AdminEditForm';
import './AdminTable.scss';
import AdminTablePagination from './admin-table-pagination/AdminTablePagination';
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
  onCreate: OnCreateFunction;
}

interface AdminTableTitleProps {
  tableName: string;
  isEmpty: boolean;
}

interface CreateButtonProps {
  onCreate: () => void;
}

function TableTitle(props: AdminTableTitleProps) {
  return (
    <h4 className="text-blue h4">
      {props.tableName} Table{props.isEmpty && ' - No current records'}
    </h4>
  );
}

function CreateButton(props: CreateButtonProps) {
  return (
    <span
      className="admin-btn btn_1"
      rel="content-y"
      role="button"
      onClick={() => props.onCreate()}>
      <i className="fa fa-plus"></i> CREATE
    </span>
  );
}

function validateList(list: AdminTableProps['list']) {
  for (let i = 0; i < list.length; i++) {
    const obj = list[i];
    const newObj: IObjectWithId = { id: obj.id };

    for (let key in obj) {
      let value = obj[key];
      const isArray = Array.isArray(value);
      const isObject = !isArray && typeof value === 'object';

      if (value === null) {
        newObj[key] = value;
        continue;
      }

      if (isObject) {
        key = `${key}Id`;
        value = value.id;
      } else if (isArray) {
        value = value.map((x: IObjectWithId) => x.id);
      }

      newObj[key] = value;
    }

    list[i] = newObj;
  }

  return list;
}

const PAGE_SIZE = 5;
export default function AdminTable(props: AdminTableProps) {
  const validatedList = useMemo(() => validateList(props.list), [props.list]);

  const [isEditing, setIsEditing] = useState(false);
  const [isCreating, setIsCreating] = useState(false);
  const [currentObj, setCurrentObj] = useState<IObjectWithId>({ id: -1 });

  const [filteredList, setFilteredList] = useState<IObjectWithId[]>(props.list);
  const { list, currentPage, pages, togglePage, nextPage, previousPage } =
    usePagination<IObjectWithId>(filteredList, PAGE_SIZE);

  const [columns, columnsLowercased] = useMemo(() => {
    let columns: string[];

    try {
      columns = Object.keys(validatedList[0]);
    } catch {
      columns = [];
    }

    return [columns, columns.map((x) => x.toLowerCase())];
  }, [validatedList, props.list]);

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

  const openCreate = () => {
    setCurrentObj({ id: -1 });
    setIsEditing(false);
    setIsCreating(true);
  };

  const openEdit = () => {
    setIsCreating(false);
    setIsEditing(true);
  };

  const closeForm = () => {
    setCurrentObj({ id: -1 });
    setIsCreating(false);
    setIsEditing(false);
  };

  const onCreate = useCallback(() => {
    if (isCreating) {
      return closeForm();
    }

    openCreate();
  }, [isCreating]);

  const onUpdate = useCallback(
    (id: number) => {
      if (isEditing && currentObj.id === id) {
        return closeForm();
      }

      const foundObj = props.list.find((x) => x.id === id);

      if (foundObj) {
        openEdit();
        setCurrentObj(foundObj);
      } else {
        closeForm();
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
        {props.create && <CreateButton onCreate={onCreate} />}
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
                {Object.values(x).map((x) => {
                  const isArray = Array.isArray(x);

                  let content = `${x}`;
                  if (isArray) {
                    content = `[${content}]`;
                  }

                  return <td key={uuidV4()}>{content}</td>;
                })}
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
            pages={pages}
            togglePage={togglePage}
            previousPage={previousPage}
            nextPage={nextPage}
          />
          <AdminTableSearch
            onSubmit={onSearch}
            columnsLowercased={columnsLowercased}
          />
        </div>
        {(isEditing || isCreating) && (
          <AdminEditForm
            currentObj={currentObj}
            columns={columns.filter((x) => x !== 'id')}
            onUpdate={props.onUpdate}
            onCreate={props.onCreate}
            closeEdit={closeForm}
            editing={isEditing}
            creating={isCreating}
          />
        )}
      </div>
    </div>
  );
}
