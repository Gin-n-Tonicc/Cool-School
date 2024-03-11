import { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Res } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import { IObjectWithId } from '../../../types/interfaces/common/IObjectWithId';
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

export default function AdminTableApi(props: AdminTableApiProps) {
  const [rows, setRows] = useState<IObjectWithId[]>([]);
  const { t } = useTranslation();

  const {
    response: getResponse,
    loading,
    get,
  } = useFetch<IObjectWithId[]>(apiUrlsConfig.admin.get(props.apiPathname));

  const { response: postResponse, post } = useFetch<IObjectWithId>(
    apiUrlsConfig.admin.post(props.apiPathname)
  );

  const { response: putResponse, put } = useFetch<IObjectWithId>(
    apiUrlsConfig.admin.updateDelete(props.apiPathname)
  );

  const { response: delResponse, del } = useFetch<void>(
    apiUrlsConfig.admin.updateDelete(props.apiPathname)
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
        `${t('admin.api.successful.create')}${obj.id}`
      );
    },
    [postResponse, onCrud, post]
  );

  const onUpdate: OnUpdateFunction = useCallback(
    async (id: number, data: Object) => {
      await put(`/${id}`, data);
      return onCrud(putResponse, `${t('admin.api.successful.update')}${id}`);
    },
    [putResponse, onCrud, put]
  );

  const onDelete: OnDeleteFunction = useCallback(
    async (id: number) => {
      await del(`/${id}`);
      onCrud(delResponse, `${t('admin.api.successful.delete')}${id}`);
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
