import { FormEventHandler } from 'react';
import { IDefaultObject } from '../../../types/interfaces/IDefaultObject';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import AdminEditFormRow from './admin-edit-form-row/AdminEditFormRow';

export type OnUpdateFunction = (id: number, body: Object) => Promise<boolean>;

interface AdminEditFormProps {
  currentObj: IObjectWithId;
  columns: string[];
  onUpdate: OnUpdateFunction;
  closeEdit: () => void;
}

export default function AdminEditForm(props: AdminEditFormProps) {
  const onSubmit: FormEventHandler<HTMLFormElement> = async (e) => {
    e.preventDefault();

    const formData = Object.fromEntries(new FormData(e.currentTarget));
    const submitData: IDefaultObject<string | null> = Object.entries(
      formData
    ).reduce((acc, [key, value]) => {
      let newValue: string | null = value.toString();

      if (!newValue) {
        newValue = null;
      }

      const newObj: IDefaultObject<string | null> = {};
      newObj[key] = newValue;

      return Object.assign(acc, newObj);
    }, {});

    if (await props.onUpdate(props.currentObj.id, submitData)) {
      props.closeEdit();
    }
  };

  return (
    <div className="mt-4">
      <h1 className="text-center">
        Editing record with ID={props.currentObj.id}
      </h1>

      <form
        onSubmit={onSubmit}
        className="d-flex flex-column align-items-center mt-4">
        <div className="w-100">
          {props.columns.map((x) => (
            <AdminEditFormRow
              key={x}
              columnName={x}
              value={props.currentObj[x]}
            />
          ))}
        </div>
        <button
          className="admin-btn btn_1 mt-4"
          style={{ cursor: 'pointer' }}
          type="submit">
          <i className="fa fa-plus"></i> EDIT
        </button>
      </form>
    </div>
  );
}
