import { useCallback, useEffect, useState } from 'react';
import { CachePolicies, Res } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import {
  OnCreateFunction,
  OnUpdateFunction,
} from '../admin-edit-form/AdminEditForm';
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
  } = useFetch<IObjectWithId[]>(apiUrlsConfig.admin.get(props.apiPathname), {
    cachePolicy: CachePolicies.NO_CACHE,
  });

  const { response: postResponse, post } = useFetch<IObjectWithId>(
    apiUrlsConfig.admin.post(props.apiPathname),
    {
      cachePolicy: CachePolicies.NO_CACHE,
    }
  );

  const { response: putResponse, put } = useFetch<IObjectWithId>(
    apiUrlsConfig.admin.updateDelete(props.apiPathname),
    {
      cachePolicy: CachePolicies.NO_CACHE,
    }
  );

  const { response: delResponse, del } = useFetch<void>(
    apiUrlsConfig.admin.updateDelete(props.apiPathname),
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
  }, [get, getResponse]);

  useEffect(() => {
    loadRows();
  }, []);

  const onCrud = useCallback(
    async (response: Res<any>, message: string) => {
      if (response.ok) {
        const promise = loadRows();
        window.alert(message);

        await promise;
        return true;
      }

      return false;
    },
    [loadRows]
  );

  const onCreate: OnCreateFunction = useCallback(
    async (data: Object) => {
      const obj = await post(data);
      return onCrud(
        postResponse,
        `Successfully created record with ID=${obj.id}`
      );
    },
    [postResponse, onCrud, post]
  );

  const onUpdate: OnUpdateFunction = useCallback(
    async (id: number, data: Object) => {
      await put(`/${id}`, data);
      return onCrud(putResponse, `Successfully updated record with ID=${id}`);
    },
    [putResponse, onCrud, put]
  );

  const onDelete: OnDeleteFunction = useCallback(
    async (id: number) => {
      await del(`/${id}`);
      onCrud(delResponse, `Successfully deleted record with ID=${id}`);
    },
    [delResponse, onCrud, del]
  );

  if (loading) {
    return <></>;
  }

  return (
    <AdminTable
      tableName={props.tableName}
      list={rows}
      create={props.create}
      update={props.update}
      delete={props.delete}
      onDelete={onDelete}
      onUpdate={onUpdate}
      onCreate={onCreate}
    />
  );
}
