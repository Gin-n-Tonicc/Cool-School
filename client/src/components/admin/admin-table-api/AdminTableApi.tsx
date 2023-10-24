import { useCallback, useEffect, useState } from 'react';
import { CachePolicies, useFetch } from 'use-http';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import Spinner from '../../common/spinner/Spinner';
import { OnUpdateFunction } from '../admin-edit-form/AdminEditForm';
import AdminTable, { OnDeleteFunction } from '../admin-table/AdminTable';

interface AdminTableApiProps {
  tableName: string;
  apiPathname: string;
  create: boolean;
  update: boolean;
  delete: boolean;
}

const baseUrl = process.env.REACT_APP_API_URL;

export default function AdminTableApi(props: AdminTableApiProps) {
  const [rows, setRows] = useState<IObjectWithId[]>([]);

  const {
    response: getResponse,
    loading,
    get,
  } = useFetch<IObjectWithId[]>(`${baseUrl}${props.apiPathname}/`, {
    cachePolicy: CachePolicies.NO_CACHE,
  });

  const { response: delResponse, del } = useFetch<void>(
    `${baseUrl}${props.apiPathname}/delete`,
    {
      cachePolicy: CachePolicies.NO_CACHE,
    }
  );

  const { response: putResponse, put } = useFetch<IObjectWithId>(
    `${baseUrl}${props.apiPathname}/update`,
    {
      cachePolicy: CachePolicies.NO_CACHE,
    }
  );

  const loadRows = useCallback(async () => {
    const data = await get();

    if (getResponse.ok) {
      setRows(data);
    } else {
      setRows([]);
    }
  }, []);

  useEffect(() => {
    loadRows();
  }, []);

  const onUpdate: OnUpdateFunction = useCallback(
    async (id: number, data: Object) => {
      const obj = await put(`/${id}`, data);

      if (putResponse.ok) {
        await loadRows();
        window.alert(`Successfully updated row with ID=${id}`);
        return true;
      }

      return false;
    },
    [putResponse]
  );

  const onDelete: OnDeleteFunction = useCallback(
    async (id: number) => {
      await del(`/${id}`);

      if (delResponse.ok) {
        await loadRows();
        window.alert(`Successfully deleted row with ID=${id}`);
      }
    },
    [delResponse]
  );

  if (loading) {
    return <Spinner />;
  }

  return (
    <AdminTable
      tableName="Users"
      list={rows}
      create={props.create}
      update={props.update}
      delete={props.delete}
      onDelete={onDelete}
      onUpdate={onUpdate}
    />
  );
}
